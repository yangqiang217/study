package com.yq.hooker.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangqiang on 2018/4/9.
 */
public class FileUtil {

    private static final String GAP = "____";
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/yq.txt";

    public static HashMap<String, String> getInfoFromSDCard() {
        HashMap<String, String> map = new HashMap<>();

        try {
            File file = new File(PATH);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] line = str.split(GAP);
                if (line.length == 2) {
                    map.put(line[0], line[1]);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            XposedLog.loge(e.getMessage());
        }
        return map;
    }

    public static void saveInfoToSDCard(HashMap<String, String> info) {
        if (info == null || info.size() == 0) {
            return;
        }

        try {
            File file = new File(PATH);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter writer = new FileWriter(file, true);

            for (Map.Entry<String, String> entry : info.entrySet()) {
                writer.write(entry.getKey() + GAP + entry.getValue() + "\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            XposedLog.loge(e.getMessage());
        }
    }

    public static boolean clearSDCard() {
        File sdcard = new File("/sdcard");
        String[] children = sdcard.list();
        if (children != null && children.length > 0) {
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(sdcard, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                //递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
