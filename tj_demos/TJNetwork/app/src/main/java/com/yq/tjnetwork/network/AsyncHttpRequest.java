package com.yq.tjnetwork.network;

import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.engine.RequestDataEngine;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

class AsyncHttpRequest implements Runnable {
    private final String TAG = "AsyncHttpResquest";// "AsyncHttpResquest";
    private final AbstractHttpClient mClient;
    private final HttpContext mContext;
    private final HttpUriRequest mRequest;
    private final AsyncHttpRespHandlerBase mResponseHandler;
    private ReqInfoTaskBase mReqInfoTask;

    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request,
                            AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase reqInfo) {
        this.mClient = client;
        this.mContext = context;
        this.mRequest = request;
        this.mResponseHandler = responseHandler;
        this.mReqInfoTask = reqInfo;
    }

    public void run() {
        try {
            if (mResponseHandler != null) {
                mResponseHandler.sendStartMsg(this.mReqInfoTask);
            }
            makeRequest();
            if (mResponseHandler != null) {
                mResponseHandler.sendFinishMsg(this.mReqInfoTask);
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            if (mResponseHandler != null) {
                mResponseHandler.sendFinishMsg(this.mReqInfoTask);
                this.mReqInfoTask.mException = new HttpResponseException(Const.NETWORK_TIMEOUT,
                        "ConnectTimeoutException");
                mResponseHandler.sendFailureMsg(this.mReqInfoTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mResponseHandler != null) {
                mResponseHandler.sendFinishMsg(this.mReqInfoTask);
                this.mReqInfoTask.mException = e;
                mResponseHandler.sendFailureMsg(this.mReqInfoTask);
            }
        } finally {
            // Todo, this should remove ?
            RequestDataEngine.getInstance().cancelTask(mReqInfoTask);
        }
        // KXLog.d(TAG, mReqInfoTask.getReqId() + " run_5");
    }

    private void makeRequest() throws IOException {
        HttpResponse response = null;
        try {
            response = mClient.execute(mRequest, mContext);//pool-1-thread-1
        } catch (IOException e) {
            //请求超时，时间由HttpConnection中设置
            mReqInfoTask.isTimeOut = true;
        }
        
        if (mReqInfoTask != null) {
            mReqInfoTask.mResponse = response;
        }
        if (mResponseHandler != null) {
            mResponseHandler.sendResponseMsg(mReqInfoTask);
        }
    }
}
