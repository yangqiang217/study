
package com.yq.tjnetwork.engine;

import android.os.Handler;
import android.os.Message;

import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.model.ModelManagerBase;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;
import com.yq.tjnetwork.network.AsyncHttpRespHandlerBase;
import com.yq.tjnetwork.network.HttpConnection;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 网络接口引擎 chengke 2013/12/19
 */
public class RequestDataEngine extends AsyncHttpRespHandlerBase {
    // 获取单一实例
    private static RequestDataEngine mInstance = null;
    private static final String TAG = "TESTAPP";// "RequestDataEngine";
    public static int ENGINE_HAVE_THE_REQUEST = -1001;
    public static int ENGINE_CREATE_MODEL_FAILURE = -1002;
    public static int ENGINE_MODEL_REQEUESTDATA_FAILURE = -1003;

    private int mReqId = 0;
    private int mConsumerId = 0;
    private HashMap<Integer, ModelManagerBase> mModelhash = new HashMap<Integer, ModelManagerBase>();
    private ArrayList<ReqInfoTaskBase> mReqInfoList = new ArrayList<ReqInfoTaskBase>();

    private HttpConnection mJsonHttpConn = new HttpConnection();
    private HttpConnection mImageHttpConn = new HttpConnection();

    public Handler mEngineHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Const.REFRESH_ACCESS_TOKEN) {
                RequestData((ReqInfoTaskBase) msg.obj);
            } else if (Const.REFRESH_ACCESS_TOKEN_SUCCESS == msg.what) {
                requestReQueue();
            }
        }
    };

    public static void postMessage2Engine(Message msg) {
        RequestDataEngine.getInstance().mEngineHandler.sendMessage(msg);
    }

    private ArrayList<ReqInfoTaskBase> mWaitingReqInfoList = new ArrayList<ReqInfoTaskBase>();

    public void addRequest2WaitingList(ReqInfoTaskBase task) {
        mWaitingReqInfoList.add(task);
    }

    public synchronized void requestReQueue() {
        cancelAllTask();
        mWaitingReqInfoList.addAll(mReqInfoList);
        mReqInfoList.clear();
        for (int i = 0; i < mWaitingReqInfoList.size(); i++) {
            this.RequestData(mWaitingReqInfoList.get(i));
        }
        mWaitingReqInfoList.clear();
    }

    protected RequestDataEngine() {

    }

    public synchronized void clear() {

        Set<Integer> keys = mModelhash.keySet();
        for (Integer key : keys) {
            ModelManagerBase m = mModelhash.get(key);
            if (m != null) {
                m.clear(-1);
            }
        }
        mModelhash.clear();
    }

    public static synchronized RequestDataEngine getInstance() {
        if (mInstance == null) {
            mInstance = new RequestDataEngine();
        }
        return mInstance;
    }

    public synchronized int getNewId() {
        return ++mReqId;
    }

    public synchronized int getConsumerId() {
        return mConsumerId++;
    }

    // 解析监听器
    DataParserTask.DataParserListener mDataParserListener = new DataParserTask.DataParserListener() {
        // @Override
        public void onDataParserFailure(ModelManagerBase model, ReqInfoTaskBase task) {
            if (model != null && task != null) {
                if (task != null) {
                    model.ParserError(task);
                } else {
                }
            } else if (task != null && model == null) {

            }
        }

        // @Override
        public void onDataParserSuccess(ModelManagerBase model, ReqInfoTaskBase task) {//main
            if (model != null && task != null) {
                if (task != null) {
                    model.ParserSuccess(task);
                } else {
                }
            } else if (task != null && model == null) {

            }
        }
    };

    /**
     * 网络操作开始会被调用
     */
    @Override
    public void onStart(ReqInfoTaskBase rf) {
    }

    /**
     * 网络操作成功时调用
     */
    @Override
    public synchronized void onSuccess(ReqInfoTaskBase rf) {
        if (rf != null) {
            mReqInfoList.remove(rf);
            ModelManagerBase model = findByInfo(rf);
            if (model != null) {
                DataParserTask dataParseTask = new DataParserTask(mDataParserListener, model, rf);
                dataParseTask.execute((String) null);
            }
            else {
            }
        }
    }

    /**
     * 网络操作失败时调用
     */
    @Override
    public synchronized void onFailure(ReqInfoTaskBase rf) {
        if (rf != null) {
            mReqInfoList.remove(rf);
            ModelManagerBase model = findByInfo(rf);
            if (model != null) {
                model.NetWorkReqFailure(rf, false);
            } else {
            }
        }
    }

    public synchronized void reAddTaskForVerify(ReqInfoTaskBase rf) {
        mReqInfoList.add(rf);
    }

    public synchronized void removeTaskForVerify(ReqInfoTaskBase rf) {
        mReqInfoList.remove(rf);
    }

    public ReqInfoTaskBase findTaskById(int nId) {
        int size = mReqInfoList.size();
        ReqInfoTaskBase task = null;
        for (int i = 0; i < size; i++) {
            task = mReqInfoList.get(i);
            if (nId == task.getReqId()) {
                break;
            }
        }
        if (task != null && task.getReqId() == nId) {
            return task;
        } else {
            return null;
        }
    }

    public ModelManagerBase findByInfo(ReqInfoTaskBase info) {
        return createModelOnNeed(info.getModelManagerType());
    }

    public synchronized ModelManagerBase findByInfo(int nType) {
        return createModelOnNeed(nType);
    }

    private synchronized ModelManagerBase createModelOnNeed(int nProType) {
        ModelManagerBase model = null;
        if (nProType == -1) {
            Assert.assertNotNull(null);
        }
        model = mModelhash.get(nProType);
        if (model == null) {
            model = ModelManagerBase.getModel(nProType);
            mModelhash.put(nProType, model);
        }
        return model;
    }

    private boolean isExist(ReqInfoTaskBase info) {
        int size = mReqInfoList.size();
        for (int i = 0; i < size; i++) {
            if (info.isEqure(mReqInfoList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public synchronized int RequestData(ReqInfoTaskBase task) {
        // KXLog.d(TAG, "RequestDataEngine requestFileImage()..."+task.mUrl);
        if (task == null) {
            return -1;
        }
        if (!isExist(task)) {
            mReqInfoList.add(task);
        } else if (task.mVerifyInfo != null) {
            // do nothing
        } else {
            // return ENGINE_HAVE_THE_REQUEST;
        }

        int nId = -1;
        ModelManagerBase model = createModelOnNeed(task.getModelManagerType());
        if (model == null) {
            return ENGINE_CREATE_MODEL_FAILURE;
        }
        ReqInfoTaskBase netTask = model.requestData(task);
        if (netTask != null) {
            netTask.setReqId(getNewId());
            mJsonHttpConn.putReq(netTask, this);
            nId = netTask.getReqId();
        } else {
            nId = ENGINE_MODEL_REQEUESTDATA_FAILURE;
        }
        task.mVerifyInfo = null;
        return nId;
    }

    /**
     * 取消指定任务
     * 
     * @param task
     */
    public void cancelTask(ReqInfoTaskBase task) {
        if (!mJsonHttpConn.cancelRequest(task)) {
            mImageHttpConn.cancelRequest(task);
        }
    }

    /**
     * 取消指定id任务
     * 
     * @param id
     */
    public void cancelTask(int id) {
        mJsonHttpConn.cancelRequest(id);
        mImageHttpConn.cancelRequest(id);
    }

    /**
     * 取消指定url任务, 如果
     *
     */
    public void cancelTask(String url) {
        mJsonHttpConn.cancelRequest(url);
        mImageHttpConn.cancelRequest(url);
    }

    /**
     * 取消指定modelType任务, 如果
     *
     */
    public void cancelTask(int modelType, Object obj) {
        mJsonHttpConn.cancelRequest(modelType, obj);
    }

    /**
     * 释放请求，当某个界面onDestroy的时候调用这个方法
     *
     */
    public void cancelActivityTask(int consumerId) {
        mImageHttpConn.cancelRequests(consumerId, true);
        mJsonHttpConn.cancelRequests(consumerId, true);
    }

    /**
     * 释放所有请求
     */
    public void cancelAllTask() {
        mJsonHttpConn.cancelAllRequests();
        mImageHttpConn.cancelAllRequests();
    }

    public void removeAllTasks() {
        mReqInfoList.clear();
        mWaitingReqInfoList.clear();
    }

}
