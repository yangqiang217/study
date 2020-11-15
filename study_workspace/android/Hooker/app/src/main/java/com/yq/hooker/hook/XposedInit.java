package com.yq.hooker.hook;

import com.yq.hooker.util.XposedLog;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by yangqiang on 2018/4/8.
 */
public class XposedInit implements IXposedHookLoadPackage {

    private static final String PACKAGE_SHAFA = "com.sohu.youju";
    private static final String PACKAGE_YINGYONGBAO = "com.tencent.android.qqdownloader";
    private static final String PACKAGE_EHR = "com.sohu.ehr";
//    private static final String PACKAGE_SHAFA = "com.example.yangqiang.test";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedLog.loge("start hook");
        try {
            HookerPhone.hook(lpparam,
                PACKAGE_SHAFA.equals(lpparam.packageName)
                    || PACKAGE_YINGYONGBAO.equals(lpparam.packageName)
                    || PACKAGE_EHR.equals(lpparam.packageName), lpparam.packageName);
        } catch (Exception e) {
            XposedLog.loge(e.getMessage());
        }


//            XposedHelpers.findAndHookMethod("com.example.yangqiang.test.MainActivity", lpparam.classLoader,
//                "onCreate", Bundle.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        //不能通过Class.forName()来获取Class ，在跨应用时会失效
//                        Class c = lpparam.classLoader.loadClass("com.example.yangqiang.test.MainActivity");
//                        Field field = c.getDeclaredField("tv");
//                        field.setAccessible(true);
//                        //param.thisObject 为执行该方法的对象，在这里指MainActivity
//                        TextView textView = (TextView) field.get(param.thisObject);
//                        textView.setText("hooked hhhhhh");
//                    }
//                });
    }
}
