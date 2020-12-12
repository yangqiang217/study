package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.aidl.Book;
import com.example.aidl.IBookManager;
import com.example.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yq on 2017/10/20.
 */

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    //不用普通ArrayList的原因是，服务端可能被多个客户端同时绑定, aild方法就被多个binder线程同时执行, 因此要保证线程同步，而CopyOnWriteArrayList已经为我们实现了操作list时的线程同步， 这样调用aidl方法时就不要考虑线程同步的问题了.
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    //保存所有客户端的对象，当图书列表发生变化时，可以遍历这个list，调用客户端的方法.
    //RemoteCallbackList是系统提供的专门用于删除跨进程listener的接口.
    //用RemoteCallbackList，而不用ArrayList的原因是, 客户端的对象注册进来后， 服务端会通过它反序列化出一个新的对象保存一起，所以说已经不是同一个对象了. 在客户端调用解除注册方法时， 在list中根本就找不到它的对象， 也就无法从list中删除客户端的对象. 而RemoteCallbackList的内部保存的是客户端对象底层的binder对象, 这个binder对象在客户端对象和反序列化的新对象中是同一个对象,  RemoteCallbackList的实现原理就是利用的这个特性.
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new RemoteCallbackList<>();

    //创建一个Binder对象
    private Binder mBinder = new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2, "Ios"));
        new Thread(new serviceWork()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //线程类。在每休眠5秒后，会自动添加一本书， 并通过OnNewBookArrived()方法通知所有观察者。
    private class serviceWork implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new Book #" + bookId);

                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将book添加到图书列表中（mBookList），并通知所有观察者
    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListeners.beginBroadcast();
        Log.e("onNewBookArrived","registener listener size:" + N);
        for (int i = 0; i < N; i++){
            IOnNewBookArrivedListener l = mListeners.getBroadcastItem(i);
            if (l!=null){
                l.OnNewBookArrivedListener(book);
            }
        }
        mListeners.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }
}
