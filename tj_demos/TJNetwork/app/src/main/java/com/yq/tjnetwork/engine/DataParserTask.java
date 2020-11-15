
package com.yq.tjnetwork.engine;

import android.os.AsyncTask;

import com.yq.tjnetwork.model.ModelManagerBase;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

/**
 * 数据解析
 * 
 * @chengke 2013/12/20
 */
public class DataParserTask extends AsyncTask<String, Void, Boolean> {

    // 数据解析回调接口
    public interface DataParserListener {
        void onDataParserSuccess (ModelManagerBase model, ReqInfoTaskBase task);

        void onDataParserFailure (ModelManagerBase model, ReqInfoTaskBase task);
    }

    private ModelManagerBase mModel = null;
    private DataParserListener mListener = null;
    private ReqInfoTaskBase mTask = null;

    public DataParserTask(DataParserListener listener, ModelManagerBase model, ReqInfoTaskBase task) {
        mListener = listener;
        mModel = model;
        mTask = task;
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        return mModel.ParserData(mTask, false);
    }

    protected void onPostExecute(Boolean result) {//main thread
        if (result) {
            mListener.onDataParserSuccess(mModel, mTask);
        }
        else {
            mListener.onDataParserFailure(mModel, mTask);
        }
    }
}
