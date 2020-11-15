package android.arch.lifecycle;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.arch.lifecycle.Lifecycle.State.CREATED;
import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.RESUMED;

/**
 * Created by liaohailiang on 2019/1/21.
 * 包名取android.arch.lifecycle就是为了能访问android.arch.lifecycle包内私有的东西
 */
public class ExternalLiveData<T> extends MutableLiveData<T> {
    private static final String TAG = "ExternalLiveData";

    public static final int START_VERSION = LiveData.START_VERSION;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        try {
            if (owner.getLifecycle().getCurrentState() == DESTROYED) {
                // ignore
                return;
            }
            LifecycleBoundObserver wrapper = new ExternalLifecycleBoundObserver(owner, observer);
            LifecycleBoundObserver existing = (LifecycleBoundObserver) callMethodPutIfAbsent(observer, wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
            }
            if (existing != null) {
                return;
            }
            getLifecycle(owner).addObserver(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            super.observe(owner, observer);
        }
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer, boolean isSticky) {
        if (getLifecycle(owner).getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        try {
            // use ExternalLifecycleBoundObserver instead of LifecycleBoundObserver
            LifecycleBoundObserver wrapper = new ExternalLifecycleBoundObserver(owner, observer);
            LifecycleBoundObserver existing = (LifecycleBoundObserver) callMethodPutIfAbsent(observer, wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer" + " with different lifecycles");
            }
            if (existing != null) {
                return;
            }
            // 通过设置mLastVersion来注册非sticky的Observer
            if (!isSticky && getVersion() > ExternalLiveData.START_VERSION) {
                wrapper.mLastVersion = getVersion();
            }
            getLifecycle(owner).addObserver(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Lifecycle getLifecycle(@NonNull LifecycleOwner owner) {
        return owner.getLifecycle();
    }

    @Override
    public int getVersion() {
        return super.getVersion();
    }

    /**
     * determine when the observer is active, means the observer can receive message
     * the default value is CREATED, means if the observer's state is above create,
     * for example, the onCreate() of activity is called
     * you can change this value to CREATED/STARTED/RESUMED
     * determine on witch state, you can receive message
     *
     * @return Lifecycle.State
     */
    protected Lifecycle.State observerActiveLevel() {
        return CREATED;
    }

    class ExternalLifecycleBoundObserver extends LifecycleBoundObserver {

        ExternalLifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<T> observer) {
            super(owner, observer);
        }

        @Override
        boolean shouldBeActive() {
            return getLifecycle(mOwner).getCurrentState().isAtLeast(RESUMED);
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            if (getLifecycle(mOwner).getCurrentState() == DESTROYED) {
                removeObserver(mObserver);
                return;
            }
            activeStateChanged(shouldBeActive());
        }

        @Override
        boolean isAttachedTo(LifecycleOwner owner) {
            return mOwner == owner;
        }

        @Override
        void detachObserver() {
            getLifecycle(mOwner).removeObserver(this);
        }
    }

    private Object getFieldObservers() throws Exception {
        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        return fieldObservers.get(this);
    }

    private Object callMethodPutIfAbsent(Object observer, Object wrapper) throws Exception {
        Object mObservers = getFieldObservers();
        Class<?> classOfSafeIterableMap = mObservers.getClass();
        Method putIfAbsent = classOfSafeIterableMap.getDeclaredMethod("putIfAbsent",
                Object.class, Object.class);
        putIfAbsent.setAccessible(true);
        return putIfAbsent.invoke(mObservers, observer, wrapper);
    }
}
