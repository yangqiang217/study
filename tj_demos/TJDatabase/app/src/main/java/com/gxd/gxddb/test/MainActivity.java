package com.gxd.gxddb.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gxd.gxddb.R;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tv;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DBOperator(this);

        tv = (TextView) findViewById(R.id.tv);

        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.query).setOnClickListener(this);
        findViewById(R.id.queryAll).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.deleteAll).setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.insert:
                TestDataInfo info1 = new TestDataInfo("1", "content1");
                TestDataManager.getInstance().insert(info1);

                TestDataInfo info2 = new TestDataInfo("2", "content2");
                TestDataManager.getInstance().insert(info2);

                TestDataInfo info3 = new TestDataInfo("3", "content3");
                TestDataManager.getInstance().insert(info3);

                showMsg("insert succ");
                break;
            case R.id.query:
                TestDataInfo i = TestDataManager.getInstance().query("1");
                if (i != null) {
                    showMsg(i.toString());
                }
                break;
            case R.id.queryAll:
                ArrayList<TestDataInfo> list = TestDataManager.getInstance().queryAll();
                StringBuilder msg = new StringBuilder();
                if (list != null) {
                    for (TestDataInfo _i: list) {
                        msg.append(_i.toString());
                        msg.append("\n");
                    }
                }
                showMsg(msg.toString());
                break;
            case R.id.update:
                TestDataInfo uI = new TestDataInfo("1", "content1_new");
                TestDataManager.getInstance().update(uI);

                showMsg("update succ");
                break;
            case R.id.delete:
                TestDataManager.getInstance().delete("1");
                showMsg("delete succ");
                break;
            case R.id.deleteAll:
                TestDataManager.getInstance().deleteAll();
                showMsg("delete all succ");
                break;
        }
    }

    private void showMsg (String msg) {
        tv.setText(msg);
    }
}
