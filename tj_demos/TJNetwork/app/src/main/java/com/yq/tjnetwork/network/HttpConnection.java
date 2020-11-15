
package com.yq.tjnetwork.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.MyApplication;
import com.yq.tjnetwork.file.MySSLSocketFactory;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;

public class HttpConnection {

    private static final String TAG = "gxdURL";

    private static final String VERSION = "1.0.0";

    private static final int MAX_CONNECTIONS = 5;// 2;
    private static final int MAX_THREAD = 5;// 2;
    private static final int SOCKET_TIMEOUT = 15 * 1000;

    private final DefaultHttpClient mHttpClient;
    private final HttpContext mHttpContext;
    private ThreadPoolExecutor mThreadPool;
    // integer is the consumer id
    private final Map<Integer, List<ReqData>> mRequestMap;
    private final Map<String, String> mClientHeaderMap;

    /**
     * Creates a new AsyncHttpClient.
     */
    public HttpConnection() {
        SSLSocketFactory sf;
        try { // 解决Android2.2 “javax.net.ssl.SSLException: Not trusted server certificate”的异常
            KeyStore trustStore;
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            sf = SSLSocketFactory.getSocketFactory();
            e.printStackTrace();
        }

        BasicHttpParams httpParams = new BasicHttpParams();

        int mSocketTimeout = SOCKET_TIMEOUT;
        ConnManagerParams.setTimeout(httpParams, mSocketTimeout);
        int mMaxConnections = MAX_CONNECTIONS;
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(mMaxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_CONNECTIONS);

        HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, mSocketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", sf, 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        mHttpContext = new SyncBasicHttpContext(new BasicHttpContext());
        mHttpClient = new DefaultHttpClient(cm, httpParams);
        mThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD);
        mRequestMap = new WeakHashMap<Integer, List<ReqData>>();
        mClientHeaderMap = new HashMap<String, String>();
    }

    /**
     * 返回内部的httpClient对象，用来对httpClient进行设置
     */
    private HttpClient getHttpClient() {
        // Set PROXY
        NetworkInfo networkInfo = ((ConnectivityManager) MyApplication.getInstance().getSystemService(
                Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            String host = android.net.Proxy.getDefaultHost();
            int port = android.net.Proxy.getDefaultPort();
            if (host != null && port != -1) {
                HttpHost httpHost = new HttpHost(host, port);
                mHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
            } else {
                mHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, null);
            }
        }

        return mHttpClient;
    }

    /**
     * 设置userAgent
     * 
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.mHttpClient.getParams(), userAgent);
    }

    /**
     * 对每个请求加一个这个header
     * 
     * @param header
     * @param value
     */
    public void addHeader(String header, String value) {
        mClientHeaderMap.put(header, value);
    }

    /**
     * 释放所有请求
     */
    public synchronized void cancelAllRequests() {
        Set<Integer> keys = mRequestMap.keySet();
        for (int key : keys) {
            cancelNoRemoveIdRequests(key, true);
        }
        mRequestMap.clear();
    }

    /**
     * 释放请求，当某个界面onDestroy的时候调用这个方法
     * 
     * @param mayInterruptIfRunning
     */
    public void cancelRequests(int consumerId, boolean mayInterruptIfRunning) {
        List<ReqData> requestList = mRequestMap.get(0);
        if (requestList != null) {
            for (ReqData requestData : requestList) {
                Future<?> request = requestData.mRequestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
        mRequestMap.remove(consumerId);
    }

    /**
     * 释放请求，当某个界面onDestroy的时候调用这个方法
     * 
     * @param mayInterruptIfRunning
     */
    public void cancelNoRemoveIdRequests(int consumerId, boolean mayInterruptIfRunning) {
        List<ReqData> requestList = mRequestMap.get(0);
        if (requestList != null) {
            for (ReqData requestData : requestList) {
                Future<?> request = requestData.mRequestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
    }

    /**
     * 取消指定任务
     * 
     * @return 是否取消了
     */
    public boolean cancelRequest(ReqInfoTaskBase task) {
        if (task == null) {
            return true;
        }
        try {
            List<ReqData> requestList = null;
            for (int key : mRequestMap.keySet()) {
                requestList = mRequestMap.get(key);
                if (requestList != null) {
                    for (ReqData reqData : requestList) {
                        if (reqData != null && reqData.mReqInfoTaskBase != null
                                && reqData.mReqInfoTaskBase.equals(task)) {
                            Future<?> future = reqData.mRequestRef.get();
                            if (future != null) {
                                future.cancel(true);
                            }
                            requestList.remove(reqData);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消指定id的任务
     * 
     * @return 是否取消了
     */
    public boolean cancelRequest(int id) {
        List<ReqData> requestList = null;
        try {
            for (int key : mRequestMap.keySet()) {
                requestList = mRequestMap.get(key);
                if (requestList != null) {
                    for (ReqData reqData : requestList) {
                        if (reqData != null && reqData.mReqInfoTaskBase.getReqId() == id) {
                            Future<?> future = reqData.mRequestRef.get();
                            if (future != null) {
                                future.cancel(true);
                            }
                            requestList.remove(reqData);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消指定url的任务
     * 
     * @return 是否取消了
     */
    public boolean cancelRequest(String url) {
        if (TextUtils.isEmpty(url)) {
            return true;
        }
        try {
            List<ReqData> requestList = null;
            for (int key : mRequestMap.keySet()) {
                requestList = mRequestMap.get(key);
                if (requestList != null) {
                    for (ReqData reqData : requestList) {
                        if (reqData != null && reqData.mReqInfoTaskBase.mUrl != null
                                && reqData.mReqInfoTaskBase.mUrl.equals(url)) {
                            Future<?> future = reqData.mRequestRef.get();
                            if (future != null) {
                                future.cancel(true);
                            }
                            requestList.remove(reqData);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消指定modelType的任务
     * 
     * @return 是否取消了
     */
    public boolean cancelRequest(int modelType, Object obj) {
        List<ReqData> requestList = null;
        try {
            for (int key : mRequestMap.keySet()) {
                requestList = mRequestMap.get(key);
                if (requestList != null) {
                    for (ReqData reqData : requestList) {
                        if (reqData != null && reqData.mReqInfoTaskBase.getModelManagerType() == modelType) {
                            Future<?> future = reqData.mRequestRef.get();
                            if (future != null) {
                                future.cancel(true);
                            }
                            requestList.remove(reqData);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void putReq(ReqInfoTaskBase rf, AsyncHttpRespHandlerBase responseHandler) {
        if (Const.ModelTypeDefine.GET_METHOD.equals(rf.mHttpType)) {
            get(rf.mUrl, rf.mParams, responseHandler, rf);
        } else if (Const.ModelTypeDefine.POST_METHOD.equals(rf.mHttpType)) {
            // Add notify handler
            rf.mParams.setAsyncHttpRespHandlerBase(responseHandler);
            rf.mParams.SettingReqInfoTaskBase(rf);

            post(rf.getUrl(), rf.getParams(), responseHandler, rf);
        } else {
            // TODO error
        }
    }

    // HTTP GET Requests
    //
    // /**
    // *
    // */
    // public void get(String url, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
    // get(null, url, null, responseHandler, ri);
    // }
    //
    // /**
    // *
    // * @param url
    // * @param params
    // * @param responseHandler
    // */
    // public void get(String url, RequestParams params, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
    // get(null, url, params, responseHandler, ri);
    // }

    /**
     * Perform a HTTP GET request without any parameters and track the Android Context which initiated the request.
     * 
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
        get(url, null, responseHandler, ri);
    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated the request.
     * 
     * @param url the URL to send the request to.
     * @param params additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, RequestParams params, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
        // FileUtil.writeAppendToFile(FileUtil.getLocationsFilePath(), getUrlWithQueryString(url, params));
        sendRequest(mHttpClient, mHttpContext, new HttpGet(getUrlWithQueryString(url, params)), null, responseHandler,
                ri);
    }

    //
    // HTTP POST Requests
    //

    // /**
    // * Perform a HTTP POST request, without any parameters.
    // * @param url the URL to send the request to.
    // * @param responseHandler the response handler instance that should handle the response.
    // */
    // public void post(String url, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
    // post(null, url, null, responseHandler, ri);
    // }
    //
    // /**
    // * Perform a HTTP POST request with parameters.
    // * @param url the URL to send the request to.
    // * @param params additional POST parameters or files to send with the request.
    // * @param responseHandler the response handler instance that should handle the response.
    // */
    // public void post(String url, RequestParams params, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri)
    // {
    // post(null, url, params, responseHandler, ri);
    // }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * 
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, RequestParams params, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
        post(url, paramsToEntity(url, params), null, responseHandler, ri);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * 
     * @param url the URL to send the request to.
     * @param entity a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml
     *            payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType the content type of the payload you are sending, for example application/json if sending a
     *            json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, HttpEntity entity, String contentType, AsyncHttpRespHandlerBase responseHandler,
                     ReqInfoTaskBase ri) {
        sendRequest(mHttpClient, mHttpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType,
                responseHandler, ri);
    }

    // Private stuff
    private void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest,
                             String contentType, AsyncHttpRespHandlerBase responseHandler, ReqInfoTaskBase ri) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }

        Future<?> request = mThreadPool.submit(new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler,
                ri));

        // if(context != null) {
        // Add request to request map
        List<ReqData> requestList = mRequestMap.get(ri.getConsumerId());
        if (requestList == null) {
            requestList = new LinkedList<ReqData>();
            mRequestMap.put(ri.getConsumerId(), requestList);
        }
        ReqData reqData = new ReqData();
        reqData.mRequestRef = new WeakReference<Future<?>>(request);
        reqData.mReqInfoTaskBase = ri;

        requestList.add(reqData);
        // }
    }

    private String getUrlWithQueryString(String url, RequestParams params) {
        if (params != null) {
            String paramString = params.getParamString(url);
            url += "?" + paramString;
        }
        return url;
    }

    private HttpEntity paramsToEntity(String url, RequestParams params) {
        HttpEntity entity = null;

        if (params != null) {
            entity = params.getEntity(url);
        }

        return entity;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase,
                                                                  HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }

        return requestBase;
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    public class ReqData {
        public WeakReference<Future<?>> mRequestRef;
        public ReqInfoTaskBase mReqInfoTaskBase;
    }
}
