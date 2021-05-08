package com.studentsystem.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.studentsystem.bean.StuCourse;
import com.studentsystem.db.DBConfig;
import com.studentsystem.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生-课程对应表Dao
 */
public class StuCourseDao {

    /** 数据表名称 */
    public static final String TABLE_NAME = "t_stu_course";

    /** 表的字段名 */
    public static String KEY_ID = "id";
    public static String KEY_STU_ID = "stuId";
    public static String KEY_COURSE_ID = "courseId";
    public static String KEY_SCORE = "score";
    public static String KEY_CREDIT = "credit";

    private SQLiteDatabase mDatabase;
    /** 数据库打开帮助类 */
    private DBManager.DBOpenHelper mDbOpenHelper;

    public void setDatabase(SQLiteDatabase db){
        mDatabase = db;
    }

    /**
     * 插入一条数据
     * @param stuCourse
     * @return
     */
    public long insertData(StuCourse stuCourse) {
        ContentValues values = new ContentValues();
        values.put(KEY_STU_ID, stuCourse.getStuId());
        values.put(KEY_COURSE_ID, stuCourse.getCourseId());
        values.put(KEY_SCORE, stuCourse.getScore());
        values.put(KEY_CREDIT, stuCourse.getCredit());
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 插入多条数据
     * @param stuCourseList
     * @return
     */
    public boolean insertData(List<StuCourse> stuCourseList) {
        //只用一个事务，提高效率
        mDatabase.beginTransaction();
        for (StuCourse stuCourse : stuCourseList) {
            long res = insertData(stuCourse);
            if (res < 0) {
                return false;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return true;
    }

    /**
     * 更新一条数据
     * 由学生id和课程id共同决定
     * @param courseId
     * @param stuId
     * @return
     */
    public long updateData(String stuId, String courseId, StuCourse stuCourse) {
        ContentValues values = new ContentValues();
        values.put(KEY_STU_ID, stuCourse.getStuId());
        values.put(KEY_COURSE_ID, stuCourse.getCourseId());
        values.put(KEY_SCORE, stuCourse.getScore());
        values.put(KEY_CREDIT, stuCourse.getCredit());
        return mDatabase.update(TABLE_NAME, values, KEY_STU_ID + "='" + stuCourse.getStuId() + "'"
            + " and " + KEY_COURSE_ID + "='" + stuCourse.getCourseId() + "'", null);
    }

    /**
     * 删除一条数据
     * @param stuCourse
     * @return
     */
    public long deleteData(StuCourse stuCourse) {
        return mDatabase.delete(TABLE_NAME, KEY_STU_ID + "='" + stuCourse.getStuId() + "'"
            + " and " + KEY_COURSE_ID + "='" + stuCourse.getCourseId() + "'", null);
    }

    /**
     * 删除多条数据
     * @param list
     * @return
     */
    public boolean deleteData(List<StuCourse> list) {
        //只用一个事务，提高效率
        mDatabase.beginTransaction();
        for (StuCourse stuCourse : list) {
            long res = deleteData(stuCourse);
            if (res < 0) {
                return false;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return true;
    }

    /**
     * 删除所有数据
     * @return
     */
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    /**
     * 查询一条数据
     * @param stuId
     * @return
     */
    public StuCourse queryData(String stuId, String courseId) {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return null;
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_STU_ID,
            KEY_COURSE_ID,
            KEY_SCORE,
            KEY_CREDIT,
        }, KEY_STU_ID + "='" + stuId + "'" + " and " + KEY_COURSE_ID + "='" + courseId + "'", null, null, null, null);
        List<StuCourse> list = convertUtil(results);
        return list != null && list.size() != 0 ? list.get(0) : null;
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<StuCourse> queryDataList() {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_STU_ID,
            KEY_COURSE_ID,
            KEY_SCORE,
            KEY_CREDIT,
        }, null, null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询某个stu选的所有课的数据
     * @return
     */
    public List<StuCourse> queryDataListByStuId(String stuId) {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_STU_ID,
            KEY_COURSE_ID,
            KEY_SCORE,
            KEY_CREDIT,
        }, KEY_STU_ID + "='" + stuId + "'", null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询选择某课的学生
     * @return
     */
    public List<StuCourse> queryDataListByCourseId(String courseId) {
        if (!DBConfig.hasData(mDatabase,TABLE_NAME)){
            return new ArrayList<>(0);
        }
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{
            KEY_STU_ID,
            KEY_COURSE_ID,
            KEY_SCORE,
            KEY_CREDIT,
        }, KEY_COURSE_ID + "='" + courseId + "'", null, null, null, null);
        return convertUtil(results);
    }

    /**
     * 查询结果转换
     * @param cursor
     * @return
     */
    private List<StuCourse> convertUtil(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return new ArrayList<>(0);
        }
        List<StuCourse> mList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
            StuCourse stuCourse = new StuCourse();
            stuCourse.setStuId(cursor.getString(cursor.getColumnIndex(KEY_STU_ID)));
            stuCourse.setCourseId(cursor.getString(cursor.getColumnIndex(KEY_COURSE_ID)));
            stuCourse.setScore(cursor.getFloat(cursor.getColumnIndex(KEY_SCORE)));
            stuCourse.setCredit(cursor.getFloat(cursor.getColumnIndex(KEY_CREDIT)));

            mList.add(stuCourse);
            cursor.moveToNext();
        }
        return mList;
    }
}
