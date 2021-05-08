package com.studentsystem.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.studentsystem.bean.Student;
import com.studentsystem.db.DBConfig;
import com.studentsystem.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生表Dao
 */
public class StudentDao {

    private static final String TAG = "StudentDao";

    /** 数据表名称 */
    public static final String TABLE_NAME = "t_student";

    /** 表的字段名 */
    public static String KEY_ID = "stuId";
    public static String KEY_PSD = "password";
    public static String KEY_NAME = "name";
    public static String KEY_AVATAR = "avatar";
    public static String KEY_BIRTHDAY = "birthday";
    public static String KEY_SEX = "sex";
    public static String KEY_PHONE = "phone";
    public static String KEY_EMAIL = "email";
    public static String KEY_HOMETOWN = "hometown";
    public static String KEY_COLLEGE = "college";
    public static String KEY_MAJOR = "major";
    public static String KEY_CLASS = "classIn";

    private SQLiteDatabase mDatabase;
    /** 数据库打开帮助类 */
    private DBManager.DBOpenHelper mDbOpenHelper;

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param student
     * @return
     */
    public long insertData(Student student) {
        long t = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, student.getStuId());
        values.put(KEY_PSD, student.getPassword());
        values.put(KEY_NAME, student.getName());
        values.put(KEY_AVATAR, student.getAvatar());
        values.put(KEY_BIRTHDAY, student.getBirthday());
        values.put(KEY_SEX, student.getSex());
        values.put(KEY_PHONE, student.getPhone());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_HOMETOWN, student.getHometown());
        values.put(KEY_COLLEGE, student.getCollege());
        values.put(KEY_MAJOR, student.getMajor());
        values.put(KEY_CLASS, student.getClassIn());
        long res = mDatabase.insert(TABLE_NAME, null, values);
        Log.e(TAG, "insert cost: " + (System.currentTimeMillis() - t));
        return res;
    }

    /**
     * 删除一条数据
     * @param stuId
     * @return
     */
    public long deleteData(String stuId) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "='" + stuId + "'", null);
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
     * @param stuId
     * @param student
     * @return
     */
    public long updateData(String stuId, Student student) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, student.getStuId());
        values.put(KEY_PSD, student.getPassword());
        values.put(KEY_NAME, student.getName());
        values.put(KEY_AVATAR, student.getAvatar());
        values.put(KEY_BIRTHDAY, student.getBirthday());
        values.put(KEY_SEX, student.getSex());
        values.put(KEY_PHONE, student.getPhone());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_HOMETOWN, student.getHometown());
        values.put(KEY_COLLEGE, student.getCollege());
        values.put(KEY_MAJOR, student.getMajor());
        values.put(KEY_CLASS, student.getClassIn());
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "='" + stuId + "'", null);
    }

    /**
     * 查询一条数据
     * @param stuId
     * @return
     */
    public Student queryData(String stuId) {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return null;
        }
        long t = System.currentTimeMillis();
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
            KEY_MAJOR,
            KEY_CLASS
        }, KEY_ID + "='" + stuId + "'", null, null, null, null);
        List<Student> list = convertUtil(results);
        Log.e(TAG, "query cost: " + (System.currentTimeMillis() - t));
        return list.size() != 0 ? list.get(0) : null;
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Student> queryDataList() {
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
            KEY_MAJOR,
            KEY_CLASS
        }, null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 根据关键字查询数据
     */
    public List<Student> queryDataListByName(String keyword) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)){
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
            KEY_MAJOR,
            KEY_CLASS
        }, KEY_NAME + " like '%" + keyword + "%'", null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 根据关键字查询数据
     */
    public List<Student> queryDataListByIds(List<String> stuIdList) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return new ArrayList<>(0);
        }
        mDatabase.beginTransaction();
        List<Student> res = new ArrayList<>();
        for (String stuId : stuIdList) {
            res.add(queryData(stuId));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return res;
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<Student> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return new ArrayList<>();
        }
        List<Student> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            Student student = new Student();
            student.setStuId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            student.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PSD)));
            student.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            student.setAvatar(cursor.getBlob(cursor.getColumnIndex(KEY_AVATAR)));
            student.setBirthday(cursor.getString(cursor.getColumnIndex(KEY_BIRTHDAY)));
            student.setSex(cursor.getString(cursor.getColumnIndex(KEY_SEX)));
            student.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            student.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            student.setHometown(cursor.getString(cursor.getColumnIndex(KEY_HOMETOWN)));
            student.setCollege(cursor.getString(cursor.getColumnIndex(KEY_COLLEGE)));
            student.setMajor(cursor.getString(cursor.getColumnIndex(KEY_MAJOR)));
            student.setClassIn(cursor.getString(cursor.getColumnIndex(KEY_CLASS)));

            mList.add(student);
            cursor.moveToNext();
        }
        return mList;
    }
}
