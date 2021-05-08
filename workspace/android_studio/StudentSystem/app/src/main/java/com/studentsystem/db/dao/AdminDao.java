package com.studentsystem.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.studentsystem.bean.Admin;
import com.studentsystem.db.DBConfig;
import com.studentsystem.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员表Dao
 */
public class AdminDao {

    /** 数据表名称 */
    public static final String TABLE_NAME = "t_admin";

    /** 表的字段名 */
    public static String KEY_ID = "adminId";
    public static String KEY_PSD = "password";
    public static String KEY_NAME = "name";
    public static String KEY_AVATAR = "avatar";
    public static String KEY_BIRTHDAY = "birthday";
    public static String KEY_SEX = "sex";
    public static String KEY_PHONE = "phone";
    public static String KEY_EMAIL = "email";
    public static String KEY_HOMETOWN = "hometown";
    public static String KEY_COLLEGE = "college";

    private SQLiteDatabase mDatabase;
    /** 数据库打开帮助类 */
    private DBManager.DBOpenHelper mDbOpenHelper;

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param admin
     * @return
     */
    public long insertData(Admin admin) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, admin.getAdminId());
        values.put(KEY_PSD, admin.getPassword());
        values.put(KEY_NAME, admin.getName());
        values.put(KEY_AVATAR, admin.getAvatar());
        values.put(KEY_BIRTHDAY, admin.getBirthday());
        values.put(KEY_SEX, admin.getSex());
        values.put(KEY_PHONE, admin.getPhone());
        values.put(KEY_EMAIL, admin.getEmail());
        values.put(KEY_HOMETOWN, admin.getHometown());
        values.put(KEY_COLLEGE, admin.getCollege());
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     * @param adminId
     * @return
     */
    public long deleteData(int adminId) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "='" + adminId + "'", null);
    }

    /**
     * 删除所有数据
     * @return
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    /**
     * 更新一条数据
     * @param adminId
     * @param admin
     * @return
     */
    public long updateData(String adminId, Admin admin) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, admin.getAdminId());
        values.put(KEY_PSD, admin.getPassword());
        values.put(KEY_NAME, admin.getName());
        values.put(KEY_AVATAR, admin.getAvatar());
        values.put(KEY_BIRTHDAY, admin.getBirthday());
        values.put(KEY_SEX, admin.getSex());
        values.put(KEY_PHONE, admin.getPhone());
        values.put(KEY_EMAIL, admin.getEmail());
        values.put(KEY_HOMETOWN, admin.getHometown());
        values.put(KEY_COLLEGE, admin.getCollege());
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "='" + adminId + "'", null);
    }

    /**
     * 查询一条数据
     * @param adminId
     * @return
     */
    public Admin queryData(String adminId) {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_PSD,
            KEY_NAME,
            KEY_AVATAR,
            KEY_BIRTHDAY,
            KEY_SEX,
            KEY_PHONE,
            KEY_EMAIL,
            KEY_HOMETOWN,
            KEY_COLLEGE,
        }, KEY_ID + "='" + adminId + "'", null, null, null, null);
        List<Admin> list = convertUtil(results);
        return list.size() != 0 ? list.get(0) : null;
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Admin> queryDataList() {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_PSD,
            KEY_NAME,
            KEY_AVATAR,
            KEY_BIRTHDAY,
            KEY_SEX,
            KEY_PHONE,
            KEY_EMAIL,
            KEY_HOMETOWN,
            KEY_COLLEGE,
        }, null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<Admin> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return new ArrayList<>(0);
        }
        List<Admin> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            Admin admin = new Admin();
            admin.setAdminId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            admin.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PSD)));
            admin.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            admin.setAvatar(cursor.getBlob(cursor.getColumnIndex(KEY_AVATAR)));
            admin.setBirthday(cursor.getString(cursor.getColumnIndex(KEY_BIRTHDAY)));
            admin.setSex(cursor.getString(cursor.getColumnIndex(KEY_SEX)));
            admin.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            admin.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            admin.setHometown(cursor.getString(cursor.getColumnIndex(KEY_HOMETOWN)));
            admin.setCollege(cursor.getString(cursor.getColumnIndex(KEY_COLLEGE)));

            mList.add(admin);
            cursor.moveToNext();
        }
        return mList;
    }
}
