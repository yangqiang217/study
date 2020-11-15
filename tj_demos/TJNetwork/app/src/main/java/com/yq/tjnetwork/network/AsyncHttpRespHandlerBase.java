
package com.yq.tjnetwork.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AsyncHttpRespHandlerBase {
    private static final int SUCCESS_MESSAGE = 0;
    private static final int FAILURE_MESSAGE = 1;
    private static final int START_MESSAGE = 2;
    private static final int FINISH_MESSAGE = 3;
    private static final int PROGRESS_MESSAGE = 4;
    private static final int TIMEOUT_MESSAGE = 5;

    private Handler mHandler;

    /**
     * Creates a new AsyncHttpRespHandler
     */
    public AsyncHttpRespHandlerBase() {
        // Set up a handler to post events back to the correct thread if possible
        if (Looper.myLooper() != null) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    AsyncHttpRespHandlerBase.this.handleMessage(msg);
                }
            };
        }
    }

    // Callbacks to be overridden

    /**
     * 网络操作开始会被调用
     */
    public void onStart(ReqInfoTaskBase rf) {
    }

    /**
     * 成功或者失败后, 调用之高速网络操作结束
     */
    public void onFinish(ReqInfoTaskBase rf) {
    }

    /**
     * 网络操作成功时调用
     */
    public void onSuccess(ReqInfoTaskBase rf) {
    }

    /**
     * 网络操作失败时调用
     */
    public void onFailure(ReqInfoTaskBase rf) {
    }

    /**
     * 网络读取数据的进度
     */
    public void onProgress(ReqInfoTaskBase rf, int percent) {
        if (rf != null && rf.getHandle() != null) {
            // KXLog.d("TESTAPP", "percent:"+percent);
            rf.mProgress = percent;
            // Message msg = obtainMessage(PROGRESS_MESSAGE, rf);
            Message msg = obtainMessage(Const.IMAGE_PROGRESS, rf);
            msg.arg1 = percent;
            rf.getHandle().sendMessage(msg);
        }
    }
    
    /**
     * 网络超时
     */
    public void onTimeOut(ReqInfoTaskBase rf) {
        if (rf != null && rf.getHandle() != null) {
            Message msg = obtainMessage(Const.NETWORK_TIMEOUT, rf);
            rf.getHandle().sendMessage(msg);
        }
    }

    // Pre-processing of messages (executes in background threadpool thread)

    protected void sendSuccessMsg(ReqInfoTaskBase rf) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, rf));
    }

    protected void sendFailureMsg(ReqInfoTaskBase rf) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, rf));
    }

    protected void sendStartMsg(ReqInfoTaskBase rf) {
        sendMessage(obtainMessage(START_MESSAGE, rf));
    }

    protected void sendFinishMsg(ReqInfoTaskBase rf) {
        sendMessage(obtainMessage(FINISH_MESSAGE, rf));
    }
    
    protected void sendTimeOutMsg(ReqInfoTaskBase rf) {
        sendMessage(obtainMessage(TIMEOUT_MESSAGE, rf));
    }

    /**
     * 发送读取的进度信息
     * 
     * @param rf
     * @param percent 进度 （整型，0 - 100）
     */
    protected void sendProgressMsg(ReqInfoTaskBase rf, int percent) {
        Message msg = obtainMessage(PROGRESS_MESSAGE, rf);
        msg.arg1 = percent;
        sendMessage(msg);
    }

    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected void handleSuccessMsg(ReqInfoTaskBase rf) {
        onSuccess(rf);
    }

    protected void handleFailureMsg(ReqInfoTaskBase rf) {
        onFailure(rf);
    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {//main thread
        ReqInfoTaskBase task = (ReqInfoTaskBase) msg.obj;
        if (task != null && task.isCancel()) {
            return;
        }
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                handleSuccessMsg((ReqInfoTaskBase) msg.obj);
                break;
            case FAILURE_MESSAGE:
                handleFailureMsg((ReqInfoTaskBase) msg.obj);
                break;
            case START_MESSAGE:
                onStart((ReqInfoTaskBase) msg.obj);
                break;
            case FINISH_MESSAGE:
                onFinish((ReqInfoTaskBase) msg.obj);
                break;
            case PROGRESS_MESSAGE:
                onProgress((ReqInfoTaskBase) msg.obj, msg.arg1);
                break;
            case TIMEOUT_MESSAGE:
                onTimeOut((ReqInfoTaskBase) msg.obj);
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object requestinfotask) {
        Message msg = null;
        if (mHandler != null) {
            msg = this.mHandler.obtainMessage(responseMessage, requestinfotask);
        } else {
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = requestinfotask;
        }
        return msg;
    }

    // Interface to AsyncHttpRequestBase
    void sendResponseMsg(ReqInfoTaskBase rf) {
        if (rf == null) {
            return;
        }
        if (rf.mResponse == null) {
            if (rf.isTimeOut) {
                sendTimeOutMsg(rf);
            } 
            return;
        }
        HttpResponse response = rf.mResponse;
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= 300) {
            rf.mException = new HttpResponseException(status.getStatusCode(), status.getReasonPhrase());
            sendFailureMsg(rf);
        } else {
            try {
                HttpEntity entity = null;
                HttpEntity temp = response.getEntity();
                if (temp != null) {
                    entity = new BufferedHttpEntity(temp);
                }
                if (rf.getModelManagerType() == Const.ModelTypeDefine.IMAGE_MODEL_MANAGER) {
                    rf.mRespByteArray = this.toByteArray(rf, entity);
                } else if (rf.getModelManagerType() == Const.ModelTypeDefine.JP_FOUND_CATEGROY_IMAGE_MODEL) {
                    rf.mRespByteArray = this.toByteArray(rf, entity);
                } else {
                    rf.mRespStr = toString(entity, null);
                }

                sendSuccessMsg(rf);
            } catch (IOException e) {
                e.printStackTrace();
                rf.mException = e;
                sendFailureMsg(rf);
            }
        }
    }

    public byte[] toByteArray(ReqInfoTaskBase rf, final HttpEntity entity) throws IOException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return new byte[] {};
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int dataLength = (int) entity.getContentLength();
        if (dataLength < 0) {
            dataLength = 4096;
        }
        ByteArrayBuffer buffer = new ByteArrayBuffer(dataLength);
        try {
            int tempLength = 4096;
            if ((dataLength / tempLength) < 6) {
                tempLength = dataLength / 6;
            }
            byte[] tmp = new byte[tempLength];
            int readLength;
            int dataPos = 0;

            int startProgress = rf.mProgress;
            int step = dataLength / 10;
            // if (rf.mProgress >= 0) {
            // KXLog.d("TESTAPP1", "toByteArray ConsumerId:" + rf.mConsumerId + ", data Length:" + dataLength +
            // ", step:" + step + ", url:"
            // + rf.mUrl);
            // }
            while ((readLength = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, readLength);

                if (rf.mProgress >= 0) {
                    if ((dataPos % step) < tempLength) { // 通知10次
                        int percent = startProgress + (dataPos * (100 - startProgress) / dataLength);
                        percent = (percent > 100) ? 100 : percent;
                        sendProgressMsg(rf, percent);
                    }
                }
                dataPos += readLength;
            }

            if (rf.mProgress >= 0) {
                sendProgressMsg(rf, 100);
            }

        } finally {
            instream.close();
        }
        return buffer.toByteArray();
    }

    public static String toString(
            final HttpEntity entity, final String defaultCharset) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static String getContentCharSet(final HttpEntity entity)
            throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;
    }
}
