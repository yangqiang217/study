package com.yq.tjnetwork.model;

import android.os.Handler;
import android.os.Message;

import com.yq.tjnetwork.Const;
import com.yq.tjnetwork.network.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取各种任务的数量: 待审核、审核已经通过的有效数、审核未通过的无效数
 */

public class CPPOIRoadSearchModelManager extends ModelManagerBase {

    private static final String TAG = "CPPOIRoadSearchModelManager"; // 方便调试

    private String jsonObj;

    public InputParam mInput = new InputParam();

    public class InputParam {

        /* 屏幕左上点所在经度 */
        public double lng_left_top;
        /* 左上点所在纬度 */
        public double lat_left_top;
        /* 屏幕右下点经度 */
        public double lng_right_bottom;
        /* 右下点经度 */
        public double lat_right_bottom;

        public void put (double lng_left_top, double lat_left_top, double lng_right_bottom, double lat_right_bottom) {
            this.lng_left_top = lng_left_top;
            this.lat_left_top = lat_left_top;
            this.lng_right_bottom = lng_right_bottom;
            this.lat_right_bottom = lat_right_bottom;
        }

        // 清理数据
        public void clear () {

        }
    }

    public static class POIRoadSearchReqInfoTask extends ReqInfoTaskBase {
        public POIRoadSearchReqInfoTask (int modelType) {
            super(modelType);
        }

        public POIRoadSearchReqInfoTask (int modelType, int nReqType, int nCount, long sCtime, Handler handle, int nConsumeId) {
            super(modelType, nReqType, sCtime, nCount, handle, nConsumeId);
        }

        @Override
        public boolean isEqure (ReqInfoTaskBase obj) {
            return (obj.getReqType() == this.getReqType() && obj.getModelManagerType() == this.getModelManagerType());
        }
    }

    @Override
    public boolean ParserData (ReqInfoTaskBase task, boolean fromCache) {//AsyncTask #1

        boolean isSuc = false;

        if (task == null || task.mRespStr != null) {
        }
        if (task != null && task.mRespStr != null && task.getHandle() != null) {

            if (task.getReqType() == Const.REFRESH || task.getReqType() == Const.FORCE_REFRESH) {
                // mPoiListData.clear();
            }
        }
        isSuc = super.ParserData(task, fromCache);

        return isSuc;
    }

    @Override
    public void ParserSuccess (ReqInfoTaskBase obj) {//main
        if (obj != null && obj.getHandle() != null) {
            Message msg = Message.obtain();
            msg.what = Const.PARSER_SUCCESS;
            msg.arg1 = obj.getReqType();
            msg.obj = obj;
            obj.getHandle().sendMessage(msg);
        }
    }

    @Override
    public ReqInfoTaskBase requestData (ReqInfoTaskBase obj) {

        super.requestData(obj);
        if (obj.getReqType() != Const.GET_CACHE_FILE) {

            obj.mHttpType = Const.ModelTypeDefine.GET_METHOD;
            // obj.mUrl = CPConst.CP_TEST_002+"/get_my_statistics?xxx=xxx"; //@todo:
            // 把URL常量值放到CPConst中
            obj.mUrl = "http://140.205.155.250/develop/daolu/search";
            obj.mParams = new RequestParams();
            obj.mParams.put("lng_left_top", String.valueOf(mInput.lng_left_top));
            obj.mParams.put("lat_left_top", String.valueOf(mInput.lat_left_top));
            obj.mParams.put("lng_right_bottom", String.valueOf(mInput.lng_right_bottom));
            obj.mParams.put("lat_right_bottom", String.valueOf(mInput.lat_right_bottom));
            this.setCommonParam(obj);

        } else {
        }
        return obj;
    }

    @Override
    public boolean parseJSON (ReqInfoTaskBase obj) {//AsyncTask #1
        String json = obj.mRespStr;
        // int nType = obj.getReqType();
        // 开始解析JSON数据 @todo
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonObj = jsonObject.toString();
            int errno = jsonObject.optInt("errno");

            // 判断接口返回值是否正确，不正确直接返回false
            if (errno != 0) {
                String errInfo = jsonObject.optString("errinfo");
                return false;
            }

            JSONArray arr = jsonObject.optJSONArray("list");
            if (arr == null) {
                return false;
            }

            return true; // 返回成功
        } catch (JSONException e) {
            e.printStackTrace();
            return false; // 返回失败
        }
    }

    public String getJsonObj () {
        return jsonObj;
    }

    @Override
    public void clear (int nConsumerId) {
    }
}
