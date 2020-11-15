package com.yq.tjnetwork;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yq.tjnetwork.model.ModelManagerBase;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 当activity调用finish()后，再接收的Message就不用处理了
            if (isFinishing()) {
                return;
            }

            // Model解析数据成功，通知子类更新UI
            if (msg.what == Const.PARSER_SUCCESS) {
                updateSuccessData(msg.arg1, msg.obj);
            } else if (msg.what == Const.PARSER_FAILURE) {
                // dismissDialog();
                ModelManagerBase.ReqInfoTaskBase task = (ModelManagerBase.ReqInfoTaskBase) msg.obj;
                if (task.getModelManagerType() == Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL) {// arg1
                    networkFailed(msg.arg1, msg.obj);
                }
            } else if (msg.what == Const.NETWORK_FAILURE) {
                dismissDialog();
            } else if(msg.what == Const.NETWORK_TIMEOUT) {
                dismissDialog();
            }
        }
    };

    /**
     * 接口调用成功，解析完数据后，通知UI更新界面，子类需要重写该方法
     *
     * @param nType
     * @param obj
     * @return
     */
    protected boolean updateSuccessData(int nType, Object obj) {
        return false;
    }

    /**
     * 网络异常通知，子类需要重写该方法
     *
     * @param nType
     * @param obj
     */
    protected void networkFailed(int nType, Object obj) {
    }

    protected void dismissDialog() {

    }
}
