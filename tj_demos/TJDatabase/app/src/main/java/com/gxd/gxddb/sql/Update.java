
package com.gxd.gxddb.sql;

import com.gxd.gxddb.dao.BaseDAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Update extends Sql {

    public Update(BaseDAO dao) {
        super(dao);
    }

    public int update(ContentValues values, String whereString) {
        SQLiteDatabase database = mDao.getDatabase();
        return database.update(mDao.getTableName(), values, whereString, null);
    }

}
