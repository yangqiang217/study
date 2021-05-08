package com.studentsystem.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.studentsystem.bean.Course;
import com.studentsystem.db.DBConfig;
import com.studentsystem.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表Dao
 */
public class CourseDao {

    /**
     * 数据表名称
     */
    public static final String TABLE_NAME = "t_course";

    /**
     * 表的字段名
     */
    public static String KEY_ID = "courseId";
    public static String KEY_NAME = "name";
    public static String KEY_TOTAL_TIME = "totalTime";
    public static String KEY_CREDIT = "credit";
    public static String KEY_TEACHER = "teacher";
    public static String KEY_OBLIGATORY = "obligatory";

    private SQLiteDatabase mDatabase;
    /**
     * 数据库打开帮助类
     */
    private DBManager.DBOpenHelper mDbOpenHelper;

    public void setDatabase(SQLiteDatabase db) {
        mDatabase = db;
    }

    /**
     * 插入一条数据
     *
     * @param course
     * @return
     */
    public long insertData(Course course) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, course.getCourseId());
        values.put(KEY_NAME, course.getName());
        values.put(KEY_TOTAL_TIME, course.getTotalTime());
        values.put(KEY_CREDIT, course.getCredit());
        values.put(KEY_TEACHER, course.getTeacher());
        values.put(KEY_OBLIGATORY, course.getObligatory());
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 删除一条数据
     *
     * @param courseId
     * @return
     */
    public long deleteData(String courseId) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "='" + courseId + "'", null);
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    /**
     * 更新一条数据
     *
     * @param courseId
     * @param course
     * @return
     */
    public long updateData(String courseId, Course course) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, course.getCourseId());
        values.put(KEY_NAME, course.getName());
        values.put(KEY_TOTAL_TIME, course.getTotalTime());
        values.put(KEY_CREDIT, course.getCredit());
        values.put(KEY_TEACHER, course.getTeacher());
        values.put(KEY_OBLIGATORY, course.getObligatory());
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "='" + courseId + "'", null);
    }

    /**
     * 查询一条数据
     *
     * @param courseId
     * @return
     */
    public Course queryData(String courseId) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_NAME,
            KEY_TOTAL_TIME,
            KEY_CREDIT,
            KEY_TEACHER,
            KEY_OBLIGATORY
        }, KEY_ID + "='" + courseId + "'", null, null, null, null);
        List<Course> list = convertUtil(results);
        return list != null && list.size() != 0 ? list.get(0) : null;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<Course> queryDataList() {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_NAME,
            KEY_TOTAL_TIME,
            KEY_CREDIT,
            KEY_TEACHER,
            KEY_OBLIGATORY
        }, null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 根据关键字查询数据
     */
    public List<Course> queryDataListByName(String keyword) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_NAME,
            KEY_TOTAL_TIME,
            KEY_CREDIT,
            KEY_TEACHER,
            KEY_OBLIGATORY
        }, KEY_NAME + " like '%" + keyword + "%'", null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 根据关键字查询数据
     */
    public List<Course> queryDataListByIds(List<String> courseIdList) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return new ArrayList<>(0);
        }
        mDatabase.beginTransaction();
        List<Course> res = new ArrayList<>();
        for (String courseId : courseIdList) {
            res.add(queryData(courseId));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return res;
    }

    /**
     * 根据授课教师名字关键字查询数据
     * 不用教师id是因为教师不一定注册了
     */
    public List<Course> queryDataListByTeacher(String keyword) {
        if (!DBConfig.hasData(mDatabase, TABLE_NAME)) {
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_ID,
            KEY_NAME,
            KEY_TOTAL_TIME,
            KEY_CREDIT,
            KEY_TEACHER,
            KEY_OBLIGATORY
        }, KEY_TEACHER + "='" + keyword + "'", null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     *
     * @param cursor
     * @return
     */
    private List<Course> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return new ArrayList<>(0);
        }
        List<Course> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            Course course = new Course();
            course.setCourseId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            course.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            course.setTotalTime(cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_TIME)));
            course.setCredit(cursor.getFloat(cursor.getColumnIndex(KEY_CREDIT)));
            course.setTeacher(cursor.getString(cursor.getColumnIndex(KEY_TEACHER)));
            course.setObligatory(cursor.getString(cursor.getColumnIndex(KEY_OBLIGATORY)));

            mList.add(course);
            cursor.moveToNext();
        }
        return mList;
    }
}
