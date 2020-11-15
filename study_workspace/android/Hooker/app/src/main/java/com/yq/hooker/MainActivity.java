package com.yq.hooker;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yq.hooker.util.FileUtil;
import com.yq.hooker.util.PermissionUtil;
import com.yq.hooker.util.PhoneInfoPool;
import com.yq.hooker.util.RandomPhoneInfoUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Button mBtnWrite, mBtnRead, mBtnClearSD;
    private TextView mTvWrite, mTvRead, mTvClearSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvWrite = findViewById(R.id.tv_write_random_info);
        mTvRead = findViewById(R.id.tv_read_random_info);
        mTvClearSD = findViewById(R.id.tv_clear_sdcard);

        mBtnWrite = findViewById(R.id.btn_write_random_info);
        mBtnRead = findViewById(R.id.btn_read_random_info);
        mBtnClearSD = findViewById(R.id.btn_clear_sdcard);
        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionUtil.onPermissionListener() {
                        @Override
                        public void onGranted() {
                            HashMap<String, String> infoMap = new HashMap<>();
                            infoMap.put(Constant.INFO_KEY.IMEI, RandomPhoneInfoUtil.createIMEI());
                            infoMap.put(Constant.INFO_KEY.WifiMac, RandomPhoneInfoUtil.createMac());

                            int index = PhoneInfoPool.INS.getRandomIndex();
                            infoMap.put(Constant.INFO_KEY.Release, PhoneInfoPool.INS.getRandomRelease());
                            infoMap.put(Constant.INFO_KEY.Brand, PhoneInfoPool.INS.getBrand(index));
                            infoMap.put(Constant.INFO_KEY.Model, PhoneInfoPool.INS.getModel(index));
                            infoMap.put(Constant.INFO_KEY.PhoneNumber, PhoneInfoPool.INS.getMobile());
                            //                infoMap.put(Constant.INFO_KEY.Screen_Width, "800");//这几个由于是非静态field，所以目前没找到hook方法
                            //                infoMap.put(Constant.INFO_KEY.Screen_Height, "600");
                            //                infoMap.put(Constant.INFO_KEY.Density, "99.9");
                            //                infoMap.put(Constant.INFO_KEY.DensityDpi, "22");
                            infoMap.put(Constant.INFO_KEY.IsRoot, "false");
                            FileUtil.saveInfoToSDCard(infoMap);

                            mTvWrite.setText("write success");
                        }

                        @Override
                        public void onRefused() {
                            showToast("sdcard permission refused");
                        }
                    });
            }
        });

        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> info = FileUtil.getInfoFromSDCard();
                for (Map.Entry<String, String> entry : info.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                mTvRead.setText("success");
            }
        });

        mBtnClearSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionUtil.onPermissionListener() {
                        @Override
                        public void onGranted() {
                            mTvClearSD.setText("start clear sdcard");
                            Observable.just(null)
                                .map(new Func1<Object, Boolean>() {
                                    @Override
                                    public Boolean call(Object o) {
                                        while (true) {
                                            boolean succ = FileUtil.clearSDCard();
                                            if (succ) break;
                                        }
                                        return true;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Boolean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        showToast("clear fail: " + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(Boolean o) {
                                        if (o) {
                                            mTvClearSD.setText("clear success");
                                        } else {
                                            mTvClearSD.setText("clear fail");
                                        }
                                    }
                                });
                        }

                        @Override
                        public void onRefused() {
                            showToast("sdcard permission refused");
                        }
                    });
            }
        });

//        String apkRoot="chmod 777 "+getPackageCodePath();
//        PermissionUtil.RootCommand(apkRoot);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvWrite.setText("");
        mTvRead.setText("");
        mTvClearSD.setText("");
    }

    /**
     * 获取cpu信息
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);

            StringBuilder res = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                res.append(str).append("\n");
            }

            br.close();
            return res.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showToast(String content) {
        Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
    }
}