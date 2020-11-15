package com.yq.hooker.hook;

import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.yq.hooker.Constant;
import com.yq.hooker.util.FileUtil;
import com.yq.hooker.util.RandomPhoneInfoUtil;
import com.yq.hooker.util.XposedLog;

import java.io.File;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookerPhone {
    private static HashMap<String, String> mHardcodedPhoneInfo = new HashMap<>();

    /**
     * @param targetPackage 是否是特定包的
     */
    public static void hook(XC_LoadPackage.LoadPackageParam param, boolean targetPackage, String packageName) {
        if (targetPackage) {
            readInfoFromSDCard();
            XposedLog
                .loge("in hook, packageName: " + packageName + ", mHardcodedPhoneInfo's size: " + mHardcodedPhoneInfo.size());

            // IMEI
            hookIMEI(param, getHookValue(Constant.INFO_KEY.IMEI));
            // PhoneNumber
            hookPhoneNumber(param, getHookValue(Constant.INFO_KEY.PhoneNumber));
            // WifiMac
            hookWifiMac(param, getHookValue(Constant.INFO_KEY.WifiMac));
            // BluetoothMac
            hookBluetoothMac(param, getHookValue(Constant.INFO_KEY.BluetoothMac));
            // Wifissid
            hookWifissid(param, getHookValue(Constant.INFO_KEY.Wifissid));
            // WifiBssid
            hookWifiBssid(param, getHookValue(Constant.INFO_KEY.WifiBssid));
            // SubscriberId
            hookSubscriberId(param, getHookValue(Constant.INFO_KEY.SubscriberId));
            // SimOperator
            hookSimOperator(param, getHookValue(Constant.INFO_KEY.SimOperator));
            // SimOperatorName
            hookSimOperatorName(param, getHookValue(Constant.INFO_KEY.SimOperatorName));
            // SimCountryIso
            hookSimCountryIso(param, getHookValue(Constant.INFO_KEY.SimCountryIso));
            // SimState
            hookSimState(param, getHookValue(Constant.INFO_KEY.SimState));
            // AndroidId
            hookAndroidId(param, getHookValue(Constant.INFO_KEY.AndroidId));
            // SDK
            hookSDK(param, getHookValue(Constant.INFO_KEY.SDK));
            // Release
            hookRelease(param, getHookValue(Constant.INFO_KEY.Release));
            // INCREMENTAL
            hookIncremental(param, getHookValue(Constant.INFO_KEY.Incremental));
            // Build.VERSION.CODENAME
            hookCODENAME(param, getHookValue(Constant.INFO_KEY.CODENAME));
            // SERIAL
            hookSerialNo(param, getHookValue(Constant.INFO_KEY.SerialNo));
            // MODEL
            hookModel(param, getHookValue(Constant.INFO_KEY.Model));
            // MANUFACTURER
            hookManufacturer(param, getHookValue(Constant.INFO_KEY.Manufacturer));
            // HARDWARE
            hookHardware(param, getHookValue(Constant.INFO_KEY.Hardware));
            // BRAND
            hookBrand(param, getHookValue(Constant.INFO_KEY.Brand));
            // BuildID
            hookBuildID(param, getHookValue(Constant.INFO_KEY.BuildID));
            // BOOTLOADER
            hookBootLoader(param, getHookValue(Constant.INFO_KEY.BootLoader));
            // RADIO
            hookRadioVersion(param, getHookValue(Constant.INFO_KEY.RadioVersion));
            // TAGS
            hookTAGS(param, getHookValue(Constant.INFO_KEY.TAGS));
            // TIME
            hookTIME(param, getHookValue(Constant.INFO_KEY.TIME));
            // TYPE
            hookTYPE(param, getHookValue(Constant.INFO_KEY.TYPE));
            // USER
            hookUSER(param, getHookValue(Constant.INFO_KEY.USER));
            // CPU_ABI
            hookCPU_ABI(param, getHookValue(Constant.INFO_KEY.CPU_ABI));
            // CPU_ABI2
            hookCPU_ABI2(param, getHookValue(Constant.INFO_KEY.CPU_ABI2));
            // BOARD
            hookBoard(param, getHookValue(Constant.INFO_KEY.Board));
            // DEVICE
            hookDevice(param, getHookValue(Constant.INFO_KEY.Device));
            // PRODUCT
            hookProduct(param, getHookValue(Constant.INFO_KEY.Product));
            //ref https://github.com/redlee90/Hide-USB-Debugging-Mode/blob/master/app/src/main/java/com/redlee90/hideusbdebugging/Tutorial.java#L42

            // USBDebugMode
            hookUSBDebugMode(param, getHookValue(Constant.INFO_KEY.USBDebugMode));
            // HOST
            hookHost(param, getHookValue(Constant.INFO_KEY.Host));
            // DISPLAY
            hookDisplay(param, getHookValue(Constant.INFO_KEY.Display));
            // FINGERPRINT
            hookFingerPrint(param, getHookValue(Constant.INFO_KEY.FingerPrint));
            // IsRoot
            hookIsRoot(param, getHookValue(Constant.INFO_KEY.IsRoot));
        }

//        //这几个目前不起作用---------
//        // Screen_Height and Width
//        hookScreenWidth(param, getHookValue(Constant.INFO_KEY.Screen_Width));
//        hookScreenHeight(param, getHookValue(Constant.INFO_KEY.Screen_Height));
//        // Density
//        hookDensity(param, getHookValue(Constant.INFO_KEY.Density));
//        // Densitydpi
//        hookDensityDPI(param, getHookValue(Constant.INFO_KEY.DensityDpi));
//        //----------------
    }

    private static void readInfoFromSDCard() {
        mHardcodedPhoneInfo = FileUtil.getInfoFromSDCard();
        if (mHardcodedPhoneInfo == null || mHardcodedPhoneInfo.size() == 0) {
            XposedLog.loge("can't get permission, generate in memory");
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.IMEI, RandomPhoneInfoUtil.createIMEI());
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.WifiMac, RandomPhoneInfoUtil.createMac());
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.Release, "randomRelease");
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.Brand, "randomBrand");
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.Model, "randomModel");
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.PhoneNumber, "13100010001");
            //                infoMap.put(Constant.INFO_KEY.Screen_Width, "800");//这几个由于是非静态field，所以目前没找到hook方法
            //                infoMap.put(Constant.INFO_KEY.Screen_Height, "600");
            //                infoMap.put(Constant.INFO_KEY.Density, "99.9");
            //                infoMap.put(Constant.INFO_KEY.DensityDpi, "22");
            mHardcodedPhoneInfo.put(Constant.INFO_KEY.IsRoot, "false");
        }
    }

    private static String getHookValue(String key) {
        return mHardcodedPhoneInfo.get(key);
    }

    private static void hookIMEI(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook IMEI, it's null");
        } else {
            final String hookValue = value;
            try {
                if (Build.VERSION.SDK_INT < 22) {  //android.os.Build.VERSION_CODES.LOLLIPOP_MR1
                    findAndHookMethod("com.android.internal.telephony.gsm.GSMPhone", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                    findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                    findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getDeviceId", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam hookParam)
                            throws Throwable {
                            hookParam.setResult(hookValue);
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                } else if (Build.VERSION.SDK_INT == 22) {  //android.os.Build.VERSION_CODES.LOLLIPOP_MR1
                    findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });

                    findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });

                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                } else if (Build.VERSION.SDK_INT >= 23) { //23 android.os.Build.VERSION_CODES.M
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", Integer.TYPE, new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", Integer.TYPE, new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                }
                XposedLog.loge("hooked IMEI with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking IMEI");
            }
        }
    }

    private static void hookPhoneNumber(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook PhoneNumber, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader,
                    "getNetworkOperator"/*"getLine1Number"*/, new Object[]{new XC_MethodHook() {

                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked PhoneNumber with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking PhoneNumber");
            }
        }
    }

    private static void hookWifiMac(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook WifiMac, it's null");
        } else {
            final String hookValue = value;
            //ref: https://developer.android.com/reference/android/net/wifi/WifiInfo.html#getMacAddress()
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getMacAddress", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked WifiMac1 with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking WifiMac1: " + e.getMessage());
            }
            ///////////////////////////

//            if (Build.VERSION.SDK_INT >= 24) {
//                //ref: https://developer.android.com/reference/android/app/admin/DevicePolicyManager.html#getWifiMacAddress(android.content.ComponentName)
//                try {
//                    findAndHookMethod("android.app.admin.DevicePolicyManager", param.classLoader, "getWifiMacAddress", ComponentName.class, new Object[]{new XC_MethodHook() {
//                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
//                                throws Throwable {
//                            hookParam.setResult(hookValue);
//                            super.afterHookedMethod(hookParam);
//                        }
//                    }
//                    });
//                    XposedLog.loge("hooked WifiMac with " + hookValue);
//                } catch (Exception e) {
//                    XposedLog.loge("error hooking WifiMac2: " + e.getMessage());
//                }
//            }

            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    findAndHookMethod("java.net.NetworkInterface", param.classLoader, "getHardwareAddress", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam hookParam)
                            throws Throwable {
                            hookParam.setResult(getByteFromMac(hookValue));
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    XposedLog.loge("hooked WifiMac2 with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking WifiMac2: " + e.getMessage());
                }
            }

        }
    }

    private static void hookBluetoothMac(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook BluetoothMac, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod("android.bluetooth.BluetoothAdapter", param.classLoader, "getAddress", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                XposedLog.loge("hooked BluetoothMac with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BluetoothMac");
            }
        }
    }

    private static void hookWifissid(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Wifissid, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getSSID", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked Wifissid with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Wifissid");
            }
        }
    }

    private static void hookWifiBssid(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook WifiBssid, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getBSSID", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked WifiBssid with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking WifiBssid");
            }
        }
    }

    private static void hookSubscriberId(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SubscriberId, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSubscriberId", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked SubscriberId with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SubscriberId");
            }
        }
    }

    private static void hookSimOperator(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SimOperator, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperator", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked SimOperator with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimOperator");
            }
        }
    }

    private static void hookSimOperatorName(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SimOperatorName, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperatorName", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked SimOperatorName with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimOperatorName");
            }
        }
    }

    private static void hookSimCountryIso(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SimCountryIso, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimCountryIso", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked SimCountryIso with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimCountryIso");
            }
        }
    }

    private static void hookSimState(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SimState, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimState", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        int nState = SIM_STATE_UNKNOWN;
                        try {
                            nState = Integer.parseInt(hookValue);
                        } catch (Exception e) {
                            nState = SIM_STATE_UNKNOWN;
                        }
                        hookParam.setResult(nState);
                    }
                }
                });
                XposedLog.loge("hooked SimState with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimState");
            }
        }
    }

    private static void hookAndroidId(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook AndroidId, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(Settings.Secure.class.getName(), param.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName(), new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam)
                        throws Throwable {
                        if (hookParam.args[1].equals("android_id"))
                            hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.loge("hooked AndroidId with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking AndroidId");
            }
        }
    }

    private static void hookSDK(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SDK, it's null");
        } else {
            int sdkInt = 19;
            try {
                sdkInt = Integer.parseInt(value);
            } catch (Exception e) {
                sdkInt = 19;
            }
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK_INT", sdkInt);
                XposedLog.loge("hooked SDK_INT with " + sdkInt);
            } catch (Exception e) {
                XposedLog.loge("error hooking SDK");
            }
        }
    }

    private static void hookRelease(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook RELEASE, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", hookValue);
                XposedLog.loge("hooked RELEASE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking RELEASE");
            }
        }
    }

    private static void hookIncremental(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook INCREMENTAL, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "INCREMENTAL", hookValue);
                XposedLog.loge("hooked INCREMENTAL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking INCREMENTAL");
            }
        }
    }

    private static void hookCODENAME(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.VERSION.CODENAME, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "CODENAME", hookValue);
                XposedLog.loge("hooked Build.VERSION.CODENAME with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.VERSION.CODENAME");
            }
        }
    }

    private static void hookSerialNo(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook SERIAL, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "SERIAL", hookValue);
                XposedLog.loge("hooked SERIAL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SERIAL");
            }
        }
    }

    private static void hookModel(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook MODEL, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MODEL", hookValue);
                XposedLog.loge("hooked MODEL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking MODEL");
            }
        }
    }

    private static void hookManufacturer(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook MODEL, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", hookValue);
                XposedLog.loge("hooked MANUFACTURER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking MANUFACTURER");
            }
        }
    }

    private static void hookHardware(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook HARDWARE, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", hookValue);
                XposedLog.loge("hooked HARDWARE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking HARDWARE");
            }
        }
    }

    private static void hookBrand(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook BRAND, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BRAND", hookValue);
                XposedLog.loge("hooked BRAND with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BRAND");
            }
        }
    }

    private static void hookBuildID(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.ID, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "ID", hookValue);
                XposedLog.loge("hooked Build.ID with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.ID");
            }
        }
    }

    private static void hookBootLoader(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.BOOTLOADER, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", hookValue);
                XposedLog.loge("hooked Build.BOOTLOADER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.BOOTLOADER");
            }
        }
    }

    private static void hookRadioVersion(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.RADIO, it's null");
        } else {
            final String hookValue = value;
            if (Build.VERSION.SDK_INT < 14) {
                try {
                    XposedHelpers.setStaticObjectField(Build.class, "RADIO", hookValue);
                    XposedLog.loge("hooked Build.RADIO with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking Build.RADIO");
                }
            } else {  //Build.VERSION.SDK_INT >= 14
                try {
                    findAndHookMethod(Build.class.getName(), param.classLoader, "getRadioVersion", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam hookParam)
                            throws Throwable {
                            hookParam.setResult(hookValue);
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    XposedLog.loge("hooked Build.RADIO with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking Build.RADIO");
                }
            }

        }
    }

    private static void hookTAGS(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.TAGS, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TAGS", hookValue);
                XposedLog.loge("hooked Build.TAGS with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TAGS");
            }
        }
    }

    private static void hookTIME(XC_LoadPackage.LoadPackageParam param, String value) {
        long buildTime = 0;
        try {
            buildTime = Integer.parseInt(value);
        } catch (Exception e) {
            buildTime = 0;
        }
        if (buildTime == 0) {
            XposedLog.loge("no hook Build.TIME, it's null or not a valid long number");
        } else {
            final long hookValue = buildTime;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TIME", hookValue);
                XposedLog.loge("hooked Build.TIME with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TIME");
            }
        }
    }

    private static void hookTYPE(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook Build.TYPE, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TYPE", hookValue);
                XposedLog.loge("hooked Build.TYPE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TYPE");
            }
        }
    }

    private static void hookUSER(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook USER, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "USER", hookValue);
                XposedLog.loge("hooked USER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking USER");
            }
        }
    }

    private static void hookCPU_ABI(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook CPU_ABI, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI", hookValue);
                XposedLog.loge("hooked CPU_ABI with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking CPU_ABI");
            }
        }
    }

    private static void hookCPU_ABI2(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook CPU_ABI2, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI2", hookValue);
                XposedLog.loge("hooked CPU_ABI2 with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking CPU_ABI2");
            }
        }
    }

    private static void hookBoard(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook BOARD, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BOARD", hookValue);
                XposedLog.loge("hooked BOARD with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BOARD");
            }
        }
    }

    private static void hookDevice(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook DEVICE, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DEVICE", hookValue);
                XposedLog.loge("hooked DEVICE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking DEVICE");
            }
        }
    }

    private static void hookProduct(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook PRODUCT, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", hookValue);
                XposedLog.loge("hooked PRODUCT with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking PRODUCT");
            }
        }
    }

    private static void hookUSBDebugMode(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook USB Debug Mode, it's null");
        } else {
            int nDebugMode = 0;
            try {
                nDebugMode = Integer.parseInt(value);
            } catch (Exception e) {
                nDebugMode = 0;
            }

            boolean bok = false;
            final int hookValue = nDebugMode;
            try {
                XposedHelpers.findAndHookMethod("android.provider.Settings.Global", param.classLoader, "getInt", ContentResolver.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals(Settings.Global.ADB_ENABLED)) {
                            param.setResult(hookValue);
                        }
                    }
                });
                bok = true;
                XposedLog.loge("hide USBDebugging: hook Settings.Global.getInt method");
            } catch (Exception e) {
                XposedLog.loge("error hooking USB Debug Mode: Settings.Global.getInt");
            }
            try {
                XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", param.classLoader, "getInt", ContentResolver.class, String.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals(Settings.Secure.ADB_ENABLED)) {
                            param.setResult(hookValue);
                        }
                    }
                });
                bok = true;
                XposedLog.loge("hide USBDebugging: hook Settings.Secure.getInt method");
            } catch (Exception e) {
                XposedLog.loge("error hooking USB Debug Mode: Settings.Secure.getInt");
            }

            if (bok) {
                XposedLog.loge("hooked USB Debug Mode with: " + hookValue);
            }
        }
    }

    private static void hookHost(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook HOST, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HOST", hookValue);
                XposedLog.loge("hooked HOST with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking HOST");
            }
        }
    }

    private static void hookDisplay(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook DISPLAY, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", hookValue);
                XposedLog.loge("hooked DISPLAY with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking DISPLAY");
            }
        }
    }

    private static void hookFingerPrint(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook FINGERPRINT, it's null");
        } else {
            final String hookValue = value;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", hookValue);
                XposedLog.loge("hooked FINGERPRINT with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking FINGERPRINT");
            }
        }
    }

    private static void hookScreenWidth(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook widthPixels, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(DisplayMetrics.class.getName(), param.classLoader, "setTo", DisplayMetrics.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam hookParam) throws Throwable {
                        DisplayMetrics displayMetrics = (DisplayMetrics) hookParam.args[0];
                        displayMetrics.widthPixels = Integer.parseInt(hookValue);
                    }
                });
                XposedLog.loge("hooked widthPixels with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking widthPixels: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private static void hookScreenHeight(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook heightPixels, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(DisplayMetrics.class.getName(), param.classLoader, "setTo", DisplayMetrics.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam hookParam) throws Throwable {
                        DisplayMetrics displayMetrics = (DisplayMetrics) hookParam.args[0];
                        displayMetrics.heightPixels = Integer.parseInt(hookValue);
                    }
                });
                XposedLog.loge("hooked heightPixels with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking heightPixels: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void hookDensity(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook density, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(DisplayMetrics.class.getName(), param.classLoader, "setTo", DisplayMetrics.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam hookParam) throws Throwable {
                        DisplayMetrics displayMetrics = (DisplayMetrics) hookParam.args[0];
                        displayMetrics.density = Float.parseFloat(hookValue);
                    }
                });
                XposedLog.loge("hooked density with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking density: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void hookDensityDPI(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook densityDpi, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(DisplayMetrics.class.getName(), param.classLoader, "setTo", DisplayMetrics.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam hookParam) throws Throwable {
                        DisplayMetrics displayMetrics = (DisplayMetrics) hookParam.args[0];
                        displayMetrics.densityDpi = Integer.parseInt(hookValue);
                    }
                });
                XposedLog.loge("hooked densityDpi with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking densityDpi: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * @param value "true" or "false"
     */
    private static void hookIsRoot(XC_LoadPackage.LoadPackageParam param, String value) {
        if (TextUtils.isEmpty(value)) {
            XposedLog.loge("no hook IsRoot, it's null");
        } else {
            final String hookValue = value;
            try {
                findAndHookMethod(File.class.getName(), param.classLoader, "exists", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam) throws Throwable {
                        if (hookParam.thisObject instanceof File) {
                            File file = (File) hookParam.thisObject;
                            if ("/system/bin/su".equals(file.getPath())
                                || "/system/xbin/su".equals(file.getPath())
                                || "/system/sbin/su".equals(file.getPath())
                                || "/sbin/su".equals(file.getPath())
                                || "/vendor/bin/su".equals(file.getPath())) {

                                XposedLog.loge("IsRoot hook exists succ");
                                hookParam.setResult("true".equals(hookValue));
                            }
                        }

                        super.afterHookedMethod(hookParam);
                    }
                }});
                findAndHookMethod(File.class.getName(), param.classLoader, "canExecute", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam hookParam) throws Throwable {
                        if (hookParam.thisObject instanceof File) {
                            File file = (File) hookParam.thisObject;
                            if ("/system/bin/su".equals(file.getPath())
                                || "/system/xbin/su".equals(file.getPath())
                                || "/system/sbin/su".equals(file.getPath())
                                || "/sbin/su".equals(file.getPath())
                                || "/vendor/bin/su".equals(file.getPath())) {

                                XposedLog.loge("IsRoot hook canExecute succ");
                                hookParam.setResult("true".equals(hookValue));
                            }
                        }
                        super.afterHookedMethod(hookParam);
                    }
                }});
                XposedLog.loge("hooked IsRoot with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking IsRoot: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * mac地址转为byte[]数组
     */
    private static byte[] getByteFromMac(String mac) {
        byte [] macBytes = new byte[6];
        String[] strArr = mac.split(":");

        for(int i = 0;i < strArr.length; i++){
            int value = Integer.parseInt(strArr[i],16);
            macBytes[i] = (byte) value;
        }
        return macBytes;
    }

}