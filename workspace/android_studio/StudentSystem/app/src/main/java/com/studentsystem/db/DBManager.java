package com.studentsystem.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.studentsystem.MyApplication;
import com.studentsystem.db.dao.AdminDao;
import com.studentsystem.db.dao.CourseDao;
import com.studentsystem.db.dao.StuCourseDao;
import com.studentsystem.db.dao.StudentDao;

import static com.studentsystem.db.DBConfig.DB_NAME;
import static com.studentsystem.db.DBConfig.DB_VERSION;

/**
 * 数据库对外总操作类
 */
public class DBManager {

    private static DBManager sDBManager;

    /** 上下文 */
    private Context mAppCtx;
    private SQLiteDatabase mDatabase;
    private DBOpenHelper mDbOpenHelper;

    /** 数据表操作类实例化 */
    private StudentDao mStudentDao;
    private CourseDao mCourseDao;
    private AdminDao mAdminDao;
    private StuCourseDao mStuCourseDao;

    public static DBManager getInstance() {
        if (sDBManager == null) {
            sDBManager = new DBManager(MyApplication.getInstance().getApplicationContext());
        }
        return sDBManager;
    }

    private DBManager(Context appCtx){
        this.mAppCtx = appCtx;

        mStudentDao = new StudentDao();
        mCourseDao = new CourseDao();
        mAdminDao = new AdminDao();
        mStuCourseDao = new StuCourseDao();
    }

    /**
     * 打开数据库
     */
    public void openDataBase() {
        mDbOpenHelper = new DBOpenHelper(mAppCtx, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
        // 设置数据库的SQLiteDatabase
        mStudentDao.setDatabase(mDatabase);
        mCourseDao.setDatabase(mDatabase);
        mAdminDao.setDatabase(mDatabase);
        mStuCourseDao.setDatabase(mDatabase);
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    /** 创建该数据库下student表的语句 */
    private static final String mStudentSqlStr = "create table if not exists " + StudentDao.TABLE_NAME + " (" +
        StudentDao.KEY_ID + " text primary key , " +
        StudentDao.KEY_PSD + " text not null , " +
        StudentDao.KEY_NAME + " text not null , " +
        StudentDao.KEY_AVATAR + " blob , " +
        StudentDao.KEY_BIRTHDAY + " text not null , " +
        StudentDao.KEY_SEX + " text not null , " +
        StudentDao.KEY_PHONE + " text not null , " +
        StudentDao.KEY_EMAIL + " text not null , " +
        StudentDao.KEY_HOMETOWN + " text not null , " +
        StudentDao.KEY_COLLEGE + " text not null , " +
        StudentDao.KEY_MAJOR + " text not null , " +
        StudentDao.KEY_CLASS + " text not null );";

    /** 创建该数据库下admin表的语句 */
    private static final String mAdminSqlStr = "create table if not exists " + AdminDao.TABLE_NAME + " (" +
        AdminDao.KEY_ID + " text primary key , " +
        AdminDao.KEY_PSD + " text not null , " +
        AdminDao.KEY_NAME + " text not null , " +
        AdminDao.KEY_AVATAR + " blob , " +
        AdminDao.KEY_BIRTHDAY + " text not null , " +
        AdminDao.KEY_SEX + " text not null , " +
        AdminDao.KEY_PHONE + " text not null , " +
        AdminDao.KEY_EMAIL + " text not null , " +
        AdminDao.KEY_HOMETOWN + " text not null , " +
        AdminDao.KEY_COLLEGE + " text not null );";

    /** 创建该数据库下course表的语句 */
    private static final String mCourseSqlStr = "create table if not exists " + CourseDao.TABLE_NAME + " (" +
        CourseDao.KEY_ID + " text primary key , " +
        CourseDao.KEY_NAME + " text not null , " +
        CourseDao.KEY_TOTAL_TIME + " integer , " +
        CourseDao.KEY_CREDIT + " float , " +
        CourseDao.KEY_TEACHER + " text not null , " +
        CourseDao.KEY_OBLIGATORY + " text not null );";

    /** 创建该数据库下student表的语句 */
    private static final String mStudentCourseSqlStr = "create table if not exists " + StuCourseDao.TABLE_NAME + " (" +
        StuCourseDao.KEY_ID + " integer primary key autoincrement , " +
        StuCourseDao.KEY_STU_ID + " text not null , " +
        StuCourseDao.KEY_COURSE_ID + " text not null , " +
        StuCourseDao.KEY_SCORE + " float , " +
        StuCourseDao.KEY_CREDIT + " float );";


    /** 删除该数据库下表的语句 */
    private static final String mStuDelSql = "DROP TABLE IF EXISTS " + StudentDao.TABLE_NAME;
    private static final String mCourseDelSql = "DROP TABLE IF EXISTS " + CourseDao.TABLE_NAME;
    private static final String mAdminDelSql = "DROP TABLE IF EXISTS " + AdminDao.TABLE_NAME;
    private static final String mStuCourseDelSql = "DROP TABLE IF EXISTS " + StuCourseDao.TABLE_NAME;


    public StudentDao getStudentDao() {
        return mStudentDao;
    }

    public CourseDao getCourseDao() {
        return mCourseDao;
    }

    public AdminDao getAdminDao() {
        return mAdminDao;
    }

    public StuCourseDao getStuCourseDao() {
        return mStuCourseDao;
    }

    /**
     * 数据表打开帮助类
     */
    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mStudentSqlStr);
            db.execSQL(mCourseSqlStr);
            db.execSQL(mAdminSqlStr);
            db.execSQL(mStudentCourseSqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(mStuDelSql);
            db.execSQL(mCourseDelSql);
            db.execSQL(mAdminDelSql);
            db.execSQL(mStuCourseDelSql);
            onCreate(db);
        }
    }
}
