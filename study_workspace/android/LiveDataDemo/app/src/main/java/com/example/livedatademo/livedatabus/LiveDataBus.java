package com.example.livedatademo.livedatabus;

import android.arch.lifecycle.ExternalLiveData;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于LiveEventBus 1.4.2版本（https://github.com/JeremyLiao/LiveEventBus）
 * 这个版本改为发送通知时判断LifeCycle的当前状态至少是Lifecycle.State.RESUMED，避免不在前台的页面收到通知
 * 比EventBus优点：不用管生命周期，退后台自动不接收；不用取消订阅
 *
 */

public class LiveDataBus {
    private final Map<String, LiveEvent<Object>> bus;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataBus DEFAULT_BUS = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return SingletonHolder.DEFAULT_BUS;
    }

    private Context appContext;
    private boolean lifecycleObserverAlwaysActive = false;
    private Config config = new Config();

    public synchronized <T> Observable<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new LiveEvent<>(key));
        }
        return (Observable<T>) bus.get(key);
    }

    public Observable<Object> with(String key) {
        return with(key, Object.class);
    }

    /**
     * use the inner class Config to set params
     * first of all, call config to get the Config instance
     * then, call the method of Config to config LiveEventBus
     * call this method in Application.onCreate
     */

    public Config config() {
        return config;
    }

    public class Config {

        /**
         * lifecycleObserverAlwaysActive
         * set if then observer can always receive message
         * true: observer can always receive message
         * false: observer can only receive message when resumed
         *
         * @param active
         * @return
         */
        public Config lifecycleObserverAlwaysActive(boolean active) {
            lifecycleObserverAlwaysActive = active;
            return this;
        }

        /**
         * config broadcast
         * only if you called this method, you can use broadcastValue() to send broadcast message
         *
         * @param context
         * @return
         */
        public Config supportBroadcast(Context context) {
            if (context != null) {
                appContext = context.getApplicationContext();
            }
//            if (appContext != null) {
//                IntentFilter intentFilter = new IntentFilter();
//                intentFilter.addAction(IpcConst.ACTION);
//                appContext.registerReceiver(receiver, intentFilter);
//            }
            return this;
        }
    }

    public interface Observable<T> {

        /**
         * 发送一个消息，支持前台线程、后台线程发送
         *
         * @param value
         */
        void post(T value);

        /**
         * 发送一个消息，支持前台线程、后台线程发送
         *
         * @param value
         */
        void postValue(T value);

        /**
         * 发送一个消息，支持前台线程、后台线程发送
         *
         * @param value
         */
        void setValue(T value);

        /**
         * 发送一个消息，支持前台线程、后台线程发送
         * 需要跨进程、跨APP发送消息的时候调用该方法
         *
         * @param value
         */
        void broadcast(T value);

        /**
         * 延迟发送一个消息，支持前台线程、后台线程发送
         *
         * @param value
         * @param delay 延迟毫秒数
         */
        void postDelay(T value, long delay);

        /**
         * 注册一个Observer，生命周期感知，自动取消订阅
         *
         * @param owner
         * @param observer
         */
        void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);

        /**
         * 注册一个Observer，生命周期感知，自动取消订阅
         * 如果之前有消息发送，可以在注册时收到消息（消息同步）
         *
         * @param owner
         * @param observer
         */
        void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);

        /**
         * 注册一个Observer
         *
         * @param observer
         */
        void observeForever(@NonNull Observer<T> observer);

        /**
         * 注册一个Observer
         * 如果之前有消息发送，可以在注册时收到消息（消息同步）
         *
         * @param observer
         */
        void observeStickyForever(@NonNull Observer<T> observer);

        /**
         * 通过observeForever或observeStickyForever注册的，需要调用该方法取消订阅
         *
         * @param observer
         */
        void removeObserver(@NonNull Observer<T> observer);
    }

    private class LiveEvent<T> implements Observable<T> {

        @NonNull
        private final String key;
        private final LifecycleLiveData<T> liveData;
        private final Map<Observer, ObserverWrapper<T>> observerMap = new HashMap<>();
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        LiveEvent(@NonNull String key) {
            this.key = key;
            this.liveData = new LifecycleLiveData<>();
        }

        @Override
        public void post(T value) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                postInternal(value);
            } else {
                mainHandler.post(new PostValueTask(value));
            }
        }

        @Override
        public void broadcast(final T value) {
            if (appContext != null) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    broadcastInternal(value);
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            broadcastInternal(value);
                        }
                    });
                }
            } else {
                post(value);
            }
        }

        @Override
        public void postDelay(T value, long delay) {
            mainHandler.postDelayed(new PostValueTask(value), delay);
        }

        @Override
        public void postValue(T value) {
            post(value);
        }

        @Override
        public void setValue(T value) {
            post(value);
        }

        @Override
        public void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                observeInternal(owner, observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeInternal(owner, observer);
                    }
                });
            }
        }

        @Override
        public void observeSticky(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                observeStickyInternal(owner, observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeStickyInternal(owner, observer);
                    }
                });
            }
        }

        @Override
        public void observeForever(@NonNull final Observer<T> observer) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                observeForeverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeForeverInternal(observer);
                    }
                });
            }
        }

        @Override
        public void observeStickyForever(@NonNull final Observer<T> observer) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                observeStickyForeverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeStickyForeverInternal(observer);
                    }
                });
            }
        }

        @Override
        public void removeObserver(@NonNull final Observer<T> observer) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                removeObserverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        removeObserverInternal(observer);
                    }
                });
            }
        }

        @MainThread
        private void postInternal(T value) {
            liveData.setValue(value);
        }

        @MainThread
        private void broadcastInternal(T value) {
//            Intent intent = new Intent(IpcConst.ACTION);
//            intent.putExtra(IpcConst.KEY, key);
//            try {
//                encoder.encode(intent, value);
//                appContext.sendBroadcast(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        @MainThread
        private void observeInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            liveData.observe(owner, observer, false);
        }

        @MainThread
        private void observeStickyInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            liveData.observe(owner, observer, true);
        }

        @MainThread
        private void observeForeverInternal(@NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            observerWrapper.preventNextEvent = liveData.getVersion() > ExternalLiveData.START_VERSION;
            observerMap.put(observer, observerWrapper);
            liveData.observeForever(observerWrapper);
        }

        @MainThread
        private void observeStickyForeverInternal(@NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            observerMap.put(observer, observerWrapper);
            liveData.observeForever(observerWrapper);
        }

        @MainThread
        private void removeObserverInternal(@NonNull Observer<T> observer) {
            if (observerMap.containsKey(observer)) {
                Observer<T> observerWrapper = observerMap.remove(observer);
                liveData.removeObserver(observerWrapper);
            }

            liveData.removeObserver(observer);
        }

        private class LifecycleLiveData<T> extends ExternalLiveData<T> {
            @Override
            protected Lifecycle.State observerActiveLevel() {
                return lifecycleObserverAlwaysActive ? Lifecycle.State.CREATED : Lifecycle.State.RESUMED;
            }

            @Override
            public void removeObserver(@NonNull Observer<T> observer) {
                super.removeObserver(observer);
                if (!liveData.hasObservers()) {
                    LiveDataBus.get().bus.remove(key);
                }
            }
        }

        private class PostValueTask implements Runnable {
            private Object newValue;

            public PostValueTask(@NonNull Object newValue) {
                this.newValue = newValue;
            }

            @Override
            public void run() {
                postInternal((T) newValue);
            }
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        @NonNull
        private final Observer<T> observer;
        private boolean preventNextEvent = false;

        ObserverWrapper(@NonNull Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (preventNextEvent) {
                preventNextEvent = false;
                return;
            }
            try {
                observer.onChanged(t);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
}
