package com.example.yq.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aidl.Book;
import com.example.aidl.IBookManager;
import com.example.aidl.IOnNewBookArrivedListener;

import java.util.List;

public class WithOtherAPPActivity extends AppCompatActivity {

    private static final String TAG = "SecondDemoActivity";
    private IBookManager bookManager;
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "received new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private ServiceConnection mService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
                Log.e(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "android进阶");
                bookManager.addBook(newBook);
                Log.e(TAG, "add book:" + newBook);
                List<Book> newList =  bookManager.getBookList();
                Log.e(TAG, "query book list:" + newList.toString());
                bookManager.registerListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
            Log.e(TAG, "binder died.");
        }
    };

    private IOnNewBookArrivedListener mNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void OnNewBookArrivedListener(Book book) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_demo);

        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.server", "com.example.server.BookManagerService"));
        bindService(intent, mService, Context.BIND_AUTO_CREATE);

        Log.e(TAG, "binding service...");
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()){
            Log.e(TAG, "unregister listener:" + mNewBookArrivedListener);
            try {
                bookManager.unregisterListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mService);
        super.onDestroy();
    }
}
