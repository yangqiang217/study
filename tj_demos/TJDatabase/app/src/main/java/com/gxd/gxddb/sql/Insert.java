
package com.gxd.gxddb.sql;

import com.gxd.gxddb.dao.BaseDAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Insert extends Sql {

    public Insert(BaseDAO dao) {
        super(dao);
    }

    public long insert(ContentValues values) {
        SQLiteDatabase database = mDao.getDatabase();
        long number = database.insertOrThrow(
                mDao.getTableName(),
                null, values);
        return number;
    }

}
