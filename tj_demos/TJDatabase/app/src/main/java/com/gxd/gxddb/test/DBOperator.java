package com.gxd.gxddb.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gxd.gxddb.BaseDB;
import com.gxd.gxddb.Database;
import com.gxd.gxddb.dao.DAOFactoryImpl;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
@Database(
        tables = {
                TestDataTable.class
        })
public class DBOperator extends BaseDB {

    public static final String DATABASE_NAME = "yq.db";

    public static final int DATABASE_VERSION = 1;

    public DBOperator(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION, DAOFactoryImpl.getInstance());
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
