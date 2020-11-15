package com.yq.tjnetwork;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yq.tjnetwork.engine.RequestDataEngine;
import com.yq.tjnetwork.model.CPPOIRoadSearchModelManager;
import com.yq.tjnetwork.model.ModelManagerBase.ReqInfoTaskBase;

/**
 * Created by YangQiang on 2016/3/21.
 */
public class MainActivity extends BaseActivity {

    private Button btnSend;
    private TextView tv;
    CPPOIRoadSearchModelManager modelRoad = (CPPOIRoadSearchModelManager) RequestDataEngine.getInstance()
            .findByInfo(
                    Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        btnSend = (Button) findViewById(R.id.btn);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                requestRoadData();
            }
        });
    }

    /**
     * 请求道路数据
     */
    private void requestRoadData () {
        CPPOIRoadSearchModelManager.POIRoadSearchReqInfoTask taskRoad = new CPPOIRoadSearchModelManager.POIRoadSearchReqInfoTask(
                Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL, Const.REFRESH, 20, -1, mHandler, 1);


        modelRoad.mInput.put(116.491221, 39.97162, 116.504405, 39.955437);

        RequestDataEngine.getInstance().RequestData(taskRoad);
    }

    @Override
    protected boolean updateSuccessData (int nType, Object obj) {
        ReqInfoTaskBase task = (ReqInfoTaskBase) obj;
        int modelType = task.getModelManagerType(); // 业务类型

        if (modelType == Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL) {//道路删除
            tv.setText(modelRoad.getJsonObj());
        }

        return true;
    }

    @Override
    protected void networkFailed (int nType, Object obj) {
        ReqInfoTaskBase task = (ReqInfoTaskBase) obj;
        int modelType = task.getModelManagerType();
        if (modelType == Const.ModelTypeDefine.AUTONAVI_POI_ROAD_SEARCH_MODEL) {//道路删除
            tv.setText(modelRoad.getJsonObj());
        }
    }
}
