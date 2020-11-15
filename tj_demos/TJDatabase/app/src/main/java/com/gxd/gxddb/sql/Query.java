
package com.gxd.gxddb.sql;

import java.util.LinkedList;
import java.util.List;
import com.gxd.gxddb.dao.BaseDAO;
import com.gxd.gxddb.orm.ORMUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @说明 这个类的主要目的是为了转换数据
 */
public class Query extends Sql {

    public Query(BaseDAO dao) {
        super(dao);
    }

    public Object query(String[] columns, String whereString, String groupBy, String orderBy, String limit) {
        SQLiteDatabase database = mDao.getDatabase(false);
        Object returnObject = null;
        Cursor c = null;
        try {
            c = database.query(
                    mDao.getTableName(),
                    columns,
                    whereString,
                    null,
                    groupBy,
                    null,
                    orderBy,
                    limit);
            if (c != null) {
                returnObject = warpData(c);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return returnObject;
    }

    @SuppressWarnings("unchecked")
	public <T> T query(String[] columns, String whereString, String groupBy, String orderBy, String limit, Class<T> type) {
        SQLiteDatabase database = mDao.getDatabase(false);
        Object returnObject = null;
        Cursor c = null;

        try {
            c = database.query(
                    mDao.getTableName(),
                    columns,
                    whereString,
                    null,
                    groupBy,
                    null,
                    orderBy,
                    limit);

			if (c != null && c.moveToFirst()) {
				returnObject = type.newInstance();
				ORMUtil.getInstance().ormQuery(type, returnObject, c);
			}
        } catch (Exception e) {
            System.out.println();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return (T) returnObject;
    }
    
	@SuppressWarnings("unchecked")
	public <T> List<T> queryAll(String[] columns, String whereString, String groupBy,
			String orderBy, String limit, Class<T> type) {
		SQLiteDatabase database = mDao.getDatabase(false);
		Object returnObject = null;
		Cursor c = null;
		try {
			c = database.query(mDao.getTableName(), columns, whereString, null,
					groupBy, null, orderBy, limit);

			if (c != null) {
				LinkedList<Object> list = new LinkedList<Object>();
				Object obj = null;
				while (c.moveToNext()) {
					obj = type.newInstance();
					ORMUtil.getInstance().ormQuery(type, obj, c);
					list.add(obj);
				}

				returnObject = list;
			}
		} catch (Exception e) {
			System.out.println();
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return (List<T>) returnObject;
	}


    /* 承载数据模型转换的重要方法 */
    public Object warpData(Cursor c) {
    	return null;
    }
}
