
package com.yq.tjnetwork.file;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.MessageDigest;

/**
 * @说明 文件工具包(支持同步和异步的方式) 包括文件路径截取集成PathUtil
 * @注: abs打头的文件路径为绝对路径
 * @未完成 ：1.文件名验证 2011-12-06
 */

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static FileUtil sInstance = new FileUtil();

    private FileUtil () {
    }

    public static FileUtil getInstance() {
        return sInstance;
    }

    /**
     * 通过URL来获得文件名
     */
    public String getFileNameFromURL(String url) {
        if (null != url) {
            String[] strs = url.split("[/]");
            // 如果之前有文件在播放,但是本文件不存在,那是否让它播放停止*?
            String fileName = strs[strs.length - 1];
            return fileName;
        }
        return null;
    }

    /**
     * 是否存在该文件
     * 
     * @param absFileName :文件的绝对路径
     */
    public boolean isExistFile(String absFileName) {
        File file = new File(absFileName);
        return file.exists();
    }

    public void createDir(String absFilePath) {
        File file = new File(absFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void createFile(String absFilePath) {
        File file = new File(absFilePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件对应字节码
     * 
     * @throws FileNotFoundException
     */
    public byte[] readBytes(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    public interface SAVE_STATE {
        int NOT_SDCARD = -1;// 没有SD
        int SAVE_OK = 0;// 存储成功
        int LOCK_SPACE = 1;// 缺少空间
        int SAVE_ERROR = 2;// 存储失败
    }

    public void deleteFile(String absFilePath) {
        String[] paths = absFilePath.split("\\|");
        for (int i = 0; i < paths.length; i++) {
            File file = new File(paths[i]);
            file.delete();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readString(Context context, String filename) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
            byte[] buffer = new byte[1024];
            int length = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] b = baos.toByteArray();
            baos.close();
            fis.close();
            return new String(b);

        } catch (Exception e) {
            return null;
        }
    }

    public void writeString(Context context, String filename, String content) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void writeString(Context context, String filename, String content, int m) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename));
            fos.write(content.getBytes());
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * MD5加密,32位 2015-01-19
     * 
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        try {
            FileInputStream fileStream = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            fileStream.close();
            byte[] md5Bytes = md5.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = (md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkApolication(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取统计 locations文件目录
     * 
     * @return
     */
    public static String getLocationsFilePath() {
//        String path = GlobalValue.getInstance().getSDPath() + CPConst.GXDTAOJIN_DOWNLOAD_PATH;
//        createDerectory(path);
//        path = path + ConstDefine.DIRECTORY_LOCATIONS_FILE_NAME;
//        File file = new File(path);
//        if (!isExist(file)) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return path;
        return null;
    }

    /**
     * 判断文件是否存在(sdcard)
     * 
     * @param file
     * @return
     */
    public static boolean isExist(File file) {

        return file.exists();

    }

    /** 创建路径 */
    public static boolean createDerectory(String dir) {
        File directory = new File(dir);
        if (directory.exists()) {
            return true;
        }
        return directory.mkdirs();
    }

    /**
     * 随机读写文件
     * 
     * @param content 文件内容
     */
    public static void randomSaveFile(String url, String content, long offset) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(url, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            // 写入内容
            randomFile.writeBytes(content);

            // randomFile.read(buffer, offset, count);
            // randomFile.read(buffer);

            // 关闭文件流描述符
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     * 
     * @param url
     * @param data
     * @return
     */
    public static boolean saveFile(final String url, final byte[] data) {
        BufferedOutputStream ostream = null;
        try {
            ostream = new BufferedOutputStream(new FileOutputStream(new File(url)));
            ostream.write(data);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ostream != null) {
                    ostream.flush();
                    ostream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取文件
     * 
     * @param url
     * @return
     */
    public static byte[] readFile(String url) {
        byte[] result = null;
        File bmpFile = new File(url);
        if (!bmpFile.exists()) {
            return null;
        }
        long fileSize = bmpFile.length();
        if (fileSize == 0) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(bmpFile);
            int len = fis.available();
            result = new byte[len];
            fis.read(result);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /** 递归的删除目录 */
    public static boolean deleteDirectory(File file) {
        if (file == null) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        }

        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                deleteDirectory(files[i]);
            }
        }
        return file.delete();
    }

    /** 删除文件，不关心返回值 */
    public static void deleteFileWithoutCheckReturnValue(File file) {
        deleteDirectory(file);
    }

    /** 删除该路径下的子文件 */
    public static void deleteSubFile(String dir) {
        if (TextUtils.isEmpty(dir)) {
            return;
        }

        try {
            File cachedFileDir = new File(dir);
            if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
                File[] cachedFiles = cachedFileDir.listFiles();
                for (File f : cachedFiles) {
                    if (f.exists() && f.isFile()) {
                        f.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 删除该路径下的最老的子文件 */
    public static void deleteOldestSubFile(String dir) {
        if (TextUtils.isEmpty(dir)) {
            return;
        }

        try {
            long minTime = System.currentTimeMillis();
            File fileToDel = null;
            File father = new File(dir);
            File[] fileList = father.listFiles();

            for (File file : fileList) {
                long modTime = file.lastModified();
                if (minTime > modTime) {
                    minTime = modTime;
                    fileToDel = file;
                }
            }
            if(fileToDel != null) {
                fileToDel.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void apply777Access(String fileName) {
        try {
            Runtime.getRuntime().exec("chmod 777 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void apply666Access(String fileName) {
        try {
            Runtime.getRuntime().exec("chmod 666 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * func: remove the file, and create the parent directly if not have been created.
     * 
     * @param file
     * @return
     */
    public static boolean makeEmptyFile(File file) {
        File parentFile = file.getParentFile();
        if (parentFile != null && parentFile.exists()) {
            if (file.exists()) {
                return file.delete();
            }
        } else if (parentFile != null && !parentFile.mkdirs()) {
            return false;
        }
        return true;
    }

    /**
     * 读取本地图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getFileBitmap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String path = getFilePath(url);
        Bitmap bitmap = null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            fis.available();
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error er) {
            er.printStackTrace();
        }

        return bitmap;
    }

    public static boolean haveFile(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String path = getFilePath(url);
        File file = null;
        try {
            file = new File(path);
            if (file.isFile()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error er) {
            er.printStackTrace();
        }
        return false;
    }

    // 需要重写
    public static String getFilePath(String url) {
        String diskCachePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "";// 自定义路径
        return diskCachePath;
    }

    /**
     * 获取best location文件目录
     * 
     * @return
     */
    public static String getBestLocationFilePath() {
//        String path = GlobalValue.getInstance().getSDPath() + CPConst.GXDTAOJIN_DOWNLOAD_PATH;
//        createDerectory(path);
//        path = path + ConstDefine.DIRECTORY_GET_BEST_LOCATION_FILE_NAME;
//        File file = new File(path);
//        if (!isExist(file)) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return path;
        return null;
    }

    /**
     * 向文件写入内容，按行追加 使用本方法前先自行用本类的isExistFile方法判断是否存在
     * 
     * @param content 写入的行数据
     */
    public void writeFileAsAppend(String path, String content) {
        File file = new File(path);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content);
            writer.newLine();// 换行
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件按每行读取数据，保留一个list，每行作为list的一个元素
     */
    public StringBuilder readFileAsLine(String path) {
        StringBuilder sb = new StringBuilder();

        File file = new File(path);
        BufferedReader reader = null;
        try {
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 10 * 1024);
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        return sb;
    }

    /**
     * 按路径+名称删除单个文件
     */
    public void deleteSingleFile(String fileLoc) {
        if (!TextUtils.isEmpty(fileLoc)) {
            File file = new File(fileLoc);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取单个文件大小
     */
    public long getSingleFileSize(String fileLoc) {
        File file = new File(fileLoc);
        return file.length();
    }

    /**
     * 拿到可以写的文件路径，如果拿的时候发现没有或者拿到的最小的都达到规定大小，就new文件并返回文件路径
     */
    public String getWritableFlePath(final String path, final long max_limit) {
        File file = new File(path);
        File[] subFile = file.listFiles();
        long min = -1;
        String minPath = null;
        boolean newFileFlag = false;
        if (subFile != null && subFile.length > 0) {// 如果底下有文件
            for (int i = 0; i < subFile.length; i++) {
                long temp = subFile[i].length();
                if (temp < min || min == -1) {
                    min = temp;
                    minPath = subFile[i].getAbsolutePath();
                }
            }
            if (min >= max_limit) {
                newFileFlag = true;
            }
        } else {// 如果底下没文件
            newFileFlag = true;
        }
        if (newFileFlag) {
            minPath = path + "/" + System.currentTimeMillis() + ".txt";
            createFile(minPath);
        }
        return minPath;
    }

    /**
     * 拿到文件夹下文件个数
     */
    public int getFileCount(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        File file = new File(path);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            return subFile.length;
        } else {
            return 0;
        }
    }

    /**
     * 取一个文件，返回路径
     */
    public String getAFilePath(String path, long max_limit) {
        max_limit += 10 * 1024;
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        File[] subFile = file.listFiles();
        String fileLoc = null;
        if (subFile != null && subFile.length > 0) {
            for (int i = 0; i < subFile.length; i++) {// 为了避免恶意在此文件夹下放不是轨迹上报的文件
                if (subFile[i] != null && subFile[i].isFile() && subFile[i].getName().matches("^\\d{13}\\.txt$") && subFile[i].length() <= max_limit) {
                    fileLoc = subFile[i].getAbsolutePath();
                    break;
                }
            }
        }
        return fileLoc;
    }
}
