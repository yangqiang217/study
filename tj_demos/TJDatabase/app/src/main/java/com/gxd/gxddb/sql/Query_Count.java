
package com.gxd.gxddb.sql;

import com.gxd.gxddb.DatabaseColumn;
import com.gxd.gxddb.dao.BaseDAO;

import android.database.Cursor;

/**
 * @说明 查询记录总数
 */
public class Query_Count extends Query {

    public Query_Count(BaseDAO dao) {
        super(dao);
    }

    @Override
    public Object warpData(Cursor c) {

        int count = 0;
        if (c.getCount() != 0) {
            c.moveToFirst();
            count = c.getInt(c.getColumnIndex(DatabaseColumn.COUNT));
        }
        return count;
    }

}
