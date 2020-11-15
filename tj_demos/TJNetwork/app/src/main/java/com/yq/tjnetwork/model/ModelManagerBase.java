/**
 * Copyright (C) 2007 - 2011 autonavi@gxd
 */

package com.yq.tjnetwork.model;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.MyApplication;
import com.yq.tjnetwork.engine.RequestDataEngine;
import com.yq.tjnetwork.file.FileCache;
import com.yq.tjnetwork.network.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求数据model基类
 * 
 * @author chengke
 */
public abstract class ModelManagerBase {
    private static final String TAG = "ModelManagerBase";
    private static long mServerTime = 0;

    public static boolean isSpecialError(int err) {
        long REFRESH_TOKEN_EXPIRED_2 = 100505;
        long REFRESH_TOKEN_EXPIRED_1 = 100504;
        long ACCESSTOKEN_EXPIRED = 100503;
        return err == ACCESSTOKEN_EXPIRED || err == REFRESH_TOKEN_EXPIRED_1
                || err == REFRESH_TOKEN_EXPIRED_2;
    }

    /* 最后一次返回的数据量 */
    protected int mLastReturnedDataNum = 0;

    protected static final int MIDLE = 0;
    protected static int MREQUEST = 1;
    protected static int MPARSER = 1;
    protected int mModelState = MIDLE;

    public static class VerifyInfo {
        public String mRcode = null;
        public String mCaptchaUrl = null;
        public String mAction = null;
        public String mCaptureData = null;
    }

    protected int mAction; // 接口类型
    protected String mVersion; // 客户端版本
    protected String mFormat = "json"; // 返回格式说明

    public static abstract class ReqInfoTaskBase {
        /** 程序/服务器需要的参数 */
        // protected Class<? extends ModelManagerBase> mModelClass;

        protected int mAction; // 接口类型
        protected String mVersion = "android-1.0.0"; // 客户端版本
        protected String mFormat = "json"; // 返回格式说明

        protected int mModelManagerType; // 业务类型
        public int mReqestType; // 请求类型，more/refresh...
        public int mConsumerId; // 数据消费者id，跟page 1-1对应
        protected int mReqId; // 请求id，engine�?
        public Handler mHandle; // Ui handle

        public int mStart; // start num 为查看更多
        private int mNum; // get item's num
        protected long mTime; // before this time

        /** 网络层需要的 */
        public String mUrl; // image url
        public String mHttpType; // get/post/put/delete
        public RequestParams mParams; // request parameters

        /** http请求数据返回的东西 */
        public Exception mException; // excepiton of the task
        public HttpResponse mResponse; // response
        public byte[] mRespByteArray; // image bytes array
        public String mRespStr; // string get from network
        public Object mImageBitmap; // bit map

        public VerifyInfo mVerifyInfo = null; // verify information

        /** 进度提示值。默认为-1，表示不显示进度条；若初始设为0表示显示进度条，完成值为100 */
        public int mProgress = -1; // progress
        
        /** 是否超时 */
        public boolean isTimeOut = false;

        private boolean bCancel = false;

        public Object mTagObj = null; //
        private String mStrPoiName; // poi name for 街边poi搜索

        public ReqInfoTaskBase(int modelType, int nReqType, Handler handle, String url,
                               int nConsumeId) {
            mModelManagerType = modelType;
            mReqestType = nReqType;
            mConsumerId = nConsumeId;
            mHandle = handle;
            mUrl = url;
        }

        public ReqInfoTaskBase(int modelType, int nReqType, long sCtime, int nCount,
                               Handler handle, int nConsumeId) {
            mModelManagerType = modelType;
            mReqestType = nReqType;
            mConsumerId = nConsumeId;
            mHandle = handle;
            mTime = sCtime;
            mNum = nCount;
        }

        public ReqInfoTaskBase(int modelType) {
            mModelManagerType = modelType;
        }

        // 默认不带餐构造
        public ReqInfoTaskBase() {

        }

        public String getStrPoiName() {
            return mStrPoiName;
        }

        public void setStrPoiName(String strPoiName) {
            this.mStrPoiName = strPoiName;
        }

        public int getModelManagerType() {
            return mModelManagerType;
        }

        public void setReqId(int nId) {
            mReqId = nId;
        }

        public int getReqId() {
            return mReqId;
        }

        public String getUrl() {
            return mUrl;
        }

        public Handler getHandle() {
            return mHandle;
        }

        public int getConsumerId() {
            return mConsumerId;
        }

        public int getCount() {
            return mNum;
        }

        public int getReqType() {
            return this.mReqestType;
        }

        public int getNum() {
            return mNum;
        }

        public long getCTime() {
            return mTime;
        }

        public RequestParams getParams() {
            return mParams;
        }

        // 设置action
        public void setAction(int action) {
            mAction = action;
        }

        // 设置channel
        public void setChannel(int nChannel) {

        }

        public void cancel() {
            bCancel = true;
        }

        public boolean isCancel() {
            return bCancel;
        }

        public abstract boolean isEqure(ReqInfoTaskBase obj);

        public void clear() {
            this.mRespStr = null;
            this.mImageBitmap = null;
            this.mRespByteArray = null;
        }
    }

    protected ModelManagerBase () {

    }

    public static synchronized long getServerTime() {
        return mServerTime;
    }

    public static synchronized void setServerTime(long serverTime) {
        mServerTime = serverTime;
    }

    protected boolean isIdle() {
        return false;
    }

    /**
     * 通用参数设置
     * 
     * @param obj 请求的task对象
     */
    public void setCommonParam(ReqInfoTaskBase obj) {
        if (obj.mParams == null) {
            obj.mParams = new RequestParams();
        }
        
        obj.mParams.put(Const.AUTONAVI_KEY_VERSION, "Android_3.2.7");
        obj.mParams.put(Const.AUTONAVI_KEY_VERIFY, "-1274509978_b4b36501fd757c44c2703f4c8c415b11_926cc6e8a67ca76d02e391e1ba3cddb8");
        obj.mParams.put(Const.AUTONAVI_KEY_IMEI, "866002025466128");
        obj.mParams.put(Const.AUTONAVI_KEY_SIGN, "13495|||||"); // 获取程序的sign
        obj.mParams.put(Const.AUTONAVI_KEY_RT, "json");
        obj.mParams.put(Const.POI_KEY_PHONE, "Android4.4.4 19 Meizu MX4 Pro");
        obj.mParams.put(Const.AUTONAVI_KEY_FROM, "gxd");
        obj.mParams.put(Const.LOGIN_NAME, "13120399041");
        obj.mParams.put(Const.LOGIN_STYLE, "1");
        obj.mParams.put(Const.LOGIN_TAO_ID, "");
    }

    /***
     * 创建数据模型对象
     * 
     * @param iType 业务类型
     * @return base 业务类型对象
     */
    public static ModelManagerBase getModel(int iType) {
        ModelManagerBase base = null;
        switch (iType) {
            case Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL: // 获取周边POI
                base = new CPPOIRoadSearchModelManager();
        }

        return base;
    }

    public String getCacheFileName() {
        return null;
    }

    protected String getJson() {
        if (getCacheFileName() == null)
            return null;
        byte[] data = FileCache.getInstance().getData(FileCache.TYPE_JSON, getCacheFileName());
        String result = null;
        try {
            result = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public abstract boolean parseJSON(ReqInfoTaskBase obj);

    /**
     * check if there are any data in the model
     * 
     * @return
     */
    protected boolean hasData() {
        return true;
    }

    /**
     * 每次UI请求数据时，如果需要从本地缓存取数据，则可以预先调用该接口 比如在无网络的时候，可能会使用缓存数据
     * 
     * @param obj
     * @return
     */
    public ReqInfoTaskBase requestData(ReqInfoTaskBase obj) {
        if (obj.getReqType() == Const.GET_CACHE_FILE) {
            RequestDataEngine.getInstance().removeTaskForVerify(obj);
        }
        
        if (obj.getReqType() == Const.GET_CACHE_FILE && !hasData()) {
            String strJson = getJson();
            if (strJson != null) {
                obj.mRespStr = strJson;
                boolean parseSuccess = ParserData(obj, true);
                if (parseSuccess) {
                    ParserSuccess(obj);
                } else {
                    ParserError(obj);
                }
            }
            if (obj.getReqType() == Const.GET_CACHE_FILE) {
                return null;
            }
        }
        return null;
    }

    /***
     * check if there are access token expired, or refresh token expired
     * 
     * @return
     */
    private boolean specialErrorCheck(ReqInfoTaskBase task) {
        boolean bRet = false;
        String json = task.mRespStr;

        if (json != null && json.length() > 0) {
            try {
                JSONObject object = new JSONObject(json);
                JSONObject result = null;
                if (object != null) {
                    result = object.optJSONObject("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bRet;
    }

    /**
     * 对服务器返回的数据进行解析
     * 
     * @param task
     * @param fromCache
     * @return
     */
    public synchronized boolean ParserData(ReqInfoTaskBase task, boolean fromCache) {//AsyncTask #1
        boolean isSuc = false;
        String json = task.mRespStr;

        if (TextUtils.isEmpty(json)) {
            return false;
        }

        if (task == null || task.mRespStr != null) {
        }
        if (task != null && task.mRespStr != null) {
            if (specialErrorCheck(task)) {

            } else {
                boolean bParseSuccess = parseJSON(task); // 开始解析json数据，此处一般调用子类的parseJson来进行数据处理
                if ((task.getReqType() == Const.REFRESH || task.getReqType() == Const.FORCE_REFRESH)
                        && bParseSuccess && !fromCache) {
                    // this.saveJson(task.mRespStr); //暂时不缓存json数据，在派生类中根据业务来进行缓存
                }
                isSuc = bParseSuccess;
            }
        }

        return isSuc;
    }

    /**
     * 用来通知UI事件 解析成功通知函数
     * 
     * @param obj
     */
    public void ParserSuccess(ReqInfoTaskBase obj) {
        if (obj != null && obj.getHandle() != null) {
            Message msg = Message.obtain();
            msg.what = Const.PARSER_SUCCESS;
            msg.arg1 = obj.getReqType();
            msg.obj = this;
            obj.getHandle().sendMessage(msg);
        }
    }

    /**
     * 删除nConsumerId 用户对应的数�?
     * 
     * @param nConsumerId
     */
    public abstract void clear(int nConsumerId);

    /**
     * 用来通知UI事件 解析失败通知函数
     * 
     * @param obj
     */
    public void ParserError(ReqInfoTaskBase obj) {
        if (obj != null && obj.getHandle() != null && obj.mVerifyInfo == null) {
            Message msg = Message.obtain();
            int nErr = 0;
            if (obj.mException instanceof HttpResponseException) {
                nErr = ((HttpResponseException) obj.mException).getStatusCode();
            }

            // 开始解析JSON数据 @todo
            try {
                JSONObject jsonObject = new JSONObject(obj.mRespStr);
                nErr = jsonObject.optInt("errno");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            msg.what = Const.PARSER_FAILURE;
            msg.arg1 = nErr;
            msg.arg2 = obj.getReqType();
            msg.obj = obj;
            obj.getHandle().sendMessage(msg);

        } else if (obj.mVerifyInfo != null) {
            // 需要输入验证码的情况
            Message msg = Message.obtain();
            msg.what = Const.VERIFY_ERROR;
            msg.arg1 = obj.getReqType();
            msg.obj = obj;
            obj.getHandle().sendMessage(msg);
        }
    }

    /**
     * 用来通知UI网络异常
     * 
     * @param obj
     * @param isCancel
     */
    public void NetWorkReqFailure(ReqInfoTaskBase obj, boolean isCancel) {
        if (obj != null && obj.getHandle() != null) {
            Message msg = Message.obtain();
            msg.what = Const.NETWORK_FAILURE;
            msg.arg1 = obj.getReqType();
            msg.obj = obj;
            obj.getHandle().sendMessage(msg);
        }
    }

    /*****
     * 用来通知UI网络传输的进度，需要引擎配合
     * 
     * @param length
     * @param pos
     * @param handle
     * @param obj
     */

    public void NetworkInprogress(int length, int pos, Handler handle, ReqInfoTaskBase obj) {

    }

    /**
     * 是否有更多数据
     * 
     * @param nId
     * @param nType
     * @return
     */
    public boolean hasGetMore(int nId, int nType) {
        return false;
    }
}
