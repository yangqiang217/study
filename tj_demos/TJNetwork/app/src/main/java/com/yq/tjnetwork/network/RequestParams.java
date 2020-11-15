
package com.yq.tjnetwork.network;

import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A collection of string request parameters or files to send along with requests made from an {@link HttpConnection}
 * instance.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * RequestParams params = new RequestParams();
 * params.put(&quot;username&quot;, &quot;james&quot;);
 * params.put(&quot;password&quot;, &quot;123456&quot;);
 * params.put(&quot;email&quot;, &quot;my@email.com&quot;);
 * params.put(&quot;profile_picture&quot;, new File(&quot;pic.jpg&quot;)); // Upload a File
 * params.put(&quot;profile_picture2&quot;, someInputStream); // Upload an InputStream
 * params.put(&quot;profile_picture3&quot;, new ByteArrayInputStream(someBytes)); // Upload some bytes
 * 
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.post(&quot;http://myendpoint.com&quot;, params, responseHandler);
 * </pre>
 */
public class RequestParams {
    private static String ENCODING = "UTF-8";

    protected ConcurrentHashMap<String, String> mUrlParams;
    protected ConcurrentHashMap<String, FileWrapper> mFileParams;

    private AsyncHttpRespHandlerBase mRresponseHandler = null;
    private ReqInfoTaskBase mReqInfoTask = null;
    
    /**
     * Constructs a new empty <code>RequestParams</code> instance.
     */
    public RequestParams() {
        init();
    }

    /**
     * Constructs a new RequestParams instance containing the key/value string params from the specified map.
     * 
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        init();

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single initial key/value string param.
     * 
     * @param key the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(String key, String value) {
        init();

        put(key, value);
    }

    public String getStringWithOrder() {

        // Set<String> keys = new TreeSet<String>(mUrlParams.keySet());
        // 生成baseString
        StringBuffer sb = new StringBuffer();

        // 去掉oauth认证
        /*
         * for (String key : keys) { if (sb.length() != 0) { sb.append("&"); } sb.append(OAuthUtil.encode(key));
         * sb.append("="); sb.append(OAuthUtil.encode(mUrlParams.get(key))); }
         */

        return sb.toString();
    }

    /**
     * set AsyncHttpRespHandlerBase for notify post grogress
     */
    public void setAsyncHttpRespHandlerBase(AsyncHttpRespHandlerBase responseHandler) {
        this.mRresponseHandler = responseHandler;
    }

    /**
     * set ReqInfoTaskBase for notify post grogress
     */
    public void SettingReqInfoTaskBase(ReqInfoTaskBase task) {
        this.mReqInfoTask = task;
    }

    /**
     * Adds a key/value string pair to the request.
     * 
     * @param key the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            mUrlParams.put(key, value);
        }
    }

    /**
     * Adds a file to the request.
     * 
     * @param key the key name for the new param.
     * @param file the file to add.
     */
    public void put(String key, File file) throws FileNotFoundException {
        put(key, new FileInputStream(file), file.getName());
    }

    /**
     * Adds an input stream to the request.
     * 
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     * 
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     */
    public void put(String key, InputStream stream, String fileName) {
        put(key, stream, fileName, null);
    }

    /**
     * Adds an input stream to the request.
     * 
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String fileName, String contentType) {
        if (key != null && stream != null) {
            mFileParams.put(key, new FileWrapper(stream, fileName, contentType));
        }
    }

    /**
     * Removes a parameter from the request.
     * 
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key) {
        mUrlParams.remove(key);
        mFileParams.remove(key);
    }
    
    //just for checking parameters whether legal or not
    public String getFromUrlParams(String key) {
    	return mUrlParams.get(key);
    }
    
    public FileWrapper getFromFileParams(String key) {
    	return mFileParams.get(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : mUrlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : mFileParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }
        
        return result.toString();
    }

    HttpEntity getEntity(String url) {
        HttpEntity entity = null;

        if (!mFileParams.isEmpty()) {
            SimpleMultiEntity multipartEntity = new SimpleMultiEntity();

            // Add notify handler
            multipartEntity.setAsyncHttpRespHandlerBase(mRresponseHandler);
            multipartEntity.SettingReqInfoTaskBase(mReqInfoTask);

            // Add string params : 加密
            List<BasicNameValuePair> paramsList = getEncryptParamsList(url);
            for(BasicNameValuePair entry : paramsList) {
                multipartEntity.addPart(entry.getName(), entry.getValue());
            }

            // Add file params
            int i = 0;
            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : mFileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if (file.inputStream != null) {
                    if (i != 0) {
                        multipartEntity.addBoundary(); // 增加boundary分隔符
                    }
                    if (file.contentType != null) {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, file.contentType);
                    } else {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream);
                    }
                }
                i++;
            }

            entity = multipartEntity;
        } else {
            try {
                List<BasicNameValuePair> paramsList = getEncryptParamsList(url);
                entity = new UrlEncodedFormEntity(paramsList, ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
    
    private List<BasicNameValuePair> getEncryptParamsList(String url) {

        List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
        paramsList = getParamsList();

        return paramsList;
    }

    private void init() {
        mUrlParams = new ConcurrentHashMap<String, String>();
        mFileParams = new ConcurrentHashMap<String, FileWrapper>();
    }

    protected List<BasicNameValuePair> getParamsList() {
        
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();
        for (ConcurrentHashMap.Entry<String, String> entry : mUrlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return lparams;
    }

    protected String getParamString(String url) {
        return URLEncodedUtils.format(getParamsList(), ENCODING);

    }

    private static class FileWrapper {
        public InputStream inputStream;
        public String fileName;
        public String contentType;

        public FileWrapper(InputStream inputStream, String fileName, String contentType) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if (fileName != null) {
                return fileName;
            } else {
                return "nofilename";
            }
        }
    }
}
