package com.example.jetpackdemo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class MainViewModel extends ViewModel {


    private Timer timer;
    private int curr;
    //    private OnTimeChangeListener onTimeChangeListener;

    //用liveData替换listener
    private MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public MutableLiveData<Integer> getLiveData() {
        return liveData;
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    curr++;
                    if (liveData != null) {
                        liveData.postValue(curr);
                    }
//                    if (onTimeChangeListener != null)
//                        onTimeChangeListener.onTimeChanged(curr);
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    //    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
//        this.onTimeChangeListener = onTimeChangeListener;
//    }

//    public interface OnTimeChangeListener {
//        void onTimeChanged(int second);
//    }
}
