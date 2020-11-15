
package com.gxd.gxddb.dao;

import com.gxd.gxddb.BaseDBTable;
import com.gxd.gxddb.DatabaseColumn;
import com.gxd.gxddb.sql.Delete;
import com.gxd.gxddb.sql.Insert;
import com.gxd.gxddb.sql.Query;
import com.gxd.gxddb.sql.Query_Count;
import com.gxd.gxddb.sql.Update;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @说明 表和DAO是一一对应关系,DAO与数据观察者之间是一对多的关系
 * @注 任何DAO的子类都不提倡直接调用Database
 */
public class BaseDAO implements DAO {

	protected Query mQuery = new Query(this);
    protected BaseDBTable mTable;

    /* 变更sql操作符 */
    protected Update mUpdate = new Update(this);
    /* 插入操作操作符 */
    protected Insert mInsert = new Insert(this);
    /* 删除操作操作符 */
    protected Delete mDelete = new Delete(this);
    /* 查询操作 */
    protected Query mQuery_Count = new Query_Count(this);

    SQLiteDatabase mDatabase = null;

    public BaseDAO(BaseDBTable table) {
        mTable = table;
    }

    public String getTableName() {
        return mTable.getTableName();
    }

    public BaseDBTable getTable() {
        return this.mTable;
    }

    /**
     * 查询表中的记录总数
     */
    public int querySumCount() {
        SQLiteDatabase database = this.getDatabase();
        int count = 0;
        Cursor cr = null;
        try {
            cr = database.query(this.mTable.getTableName(), new String[] {
                    "count(1)"
            }, null, null, null, null, null);
            if (cr != null) {
                cr.moveToFirst();
                count = cr.getInt(cr.getColumnIndex("count(1)"));
            }
        } finally {
            if (cr != null) {
                try {
                    cr.close();
                } catch (Exception e) {
                }
            }
        }
        return count;
    }

    /**
     * 根据条件查询表中的记录总数
     */
    public int querySumCount(String where) {
        SQLiteDatabase database = this.getDatabase();
        int count = 0;
        Cursor cr = null;
        try {
            cr = database.query(this.mTable.getTableName(), new String[] {
                    "count(1)"
            }, where, null, null, null, null);
            if (cr != null) {
                cr.moveToFirst();
                count = cr.getInt(cr.getColumnIndex("count(1)"));
            }
        } finally {
            if (cr != null) {
                try {
                    cr.close();
                } catch (Exception e) {
                }
            }
        }
        return count;
    }

    public boolean isContainRecord() {
        return this.querySumCount() > 0;
    }

    /**
     * @说明 将条件数组拼接为where语句
     */
    public final String createWhereString(String[] whereStrings) {
        String whereStr = null;
        if (null != whereStrings && whereStrings.length > 0) {
            StringBuffer buffer = new StringBuffer();
            for (String str : whereStrings) {
                buffer.append(str).append(" and ");
            }
            buffer.append("1=1");
            whereStr = buffer.toString();
        }
        return whereStr;
    }

    /**
     * @方法说明 查询操作 注{ 查询的目的是为了得到一个返回结果(通过匹配query的实现类可以帮你封装好一个ORM的类模型) }
     * @param query 查询器,承载ORM之间的装换
     * @param columns 所要查询的列
     * @param groupBy sql中分组列名
     * @param orderBy 排序方式 形如 name desc
     * @param limit 限制条数 形如 1,2表示从第1条开始的两条(含1条)结果最多为2条
     */
    protected final Object query1(Query query, String[] columns, String[] whereStrings, String groupBy, String orderBy,
            String limit) {
        return this.query2(query, columns, this.createWhereString(whereStrings), groupBy, orderBy, limit);
    }

    protected final Object query2(Query query, String[] columns, String whereString, String groupBy, String orderBy,
            String limit) {
        Object o = query.query(columns, whereString, groupBy, orderBy, limit);
        return o;
    }

    /**
     * @方法说明 删除操作
     */

    protected final void delete1(String[] whereStrings) {
        this.delete2(this.createWhereString(whereStrings));
    }

    protected final long delete2(String whereString) {
        return mDelete.delete(whereString);
    }

    /**
     * @方法说明 变更操作
     * @param values 表示要变更的属性对应表
     */
    protected final int update1(String[] whereStrings, ContentValues values) {
        return this.update2(this.createWhereString(whereStrings), values);
    }

    protected final int update2(String whereString, ContentValues values) {
        return mUpdate.update(values, whereString);
    }

    /**
     * @方法说明 插入操作
     * @param values 表示要插入的属性对应表
     */
    protected final long insert(ContentValues values) {
        return mInsert.insert(values);
    }

    /**
     * @方法说明 得到表中的记录数目
     */
    public final int getTableRecordCount1(String[] whereStrings) {
        return this.getTableRecordCount2(createWhereString(whereStrings));
    }

    public final int getTableRecordCount2(String whereString) {
        int count = (Integer) mQuery_Count.query(new String[] {
                DatabaseColumn.COUNT
        }, whereString, null, null, null);
        return count;
    }

    /**
     * @方法说明 开启事务 与{@link BaseDAO#commit()}成对出现
     */
    protected void beginTransaction() {
        SQLiteDatabase database = this.getDatabase();
        database.beginTransaction();
    }

    /**
     * @方法说明 提交事务 与{@link BaseDAO#beginTransaction()}成对出现
     */
    protected void commit() {
        this.getDatabase().setTransactionSuccessful();
        this.getDatabase().endTransaction();
    }

    /**
     * 得到数据库的实例
     */
    public SQLiteDatabase getDatabase() {
        return this.getDatabase(true);
    }

    static final byte[] LOCK = new byte[0];

    @SuppressWarnings("deprecation")
	public SQLiteDatabase getDatabase(boolean isWrite) {
        synchronized (LOCK) {
            if (mDatabase == null) {
                mDatabase = this.mTable.getDatabase().getWritableDatabase();
                mDatabase.setLockingEnabled(false);
            } else if (!mDatabase.isOpen()) {
                mDatabase = null;
                mDatabase = this.mTable.getDatabase().getWritableDatabase();
                mDatabase.setLockingEnabled(false);
            }
            mDatabase.setLockingEnabled(false);
            return mDatabase;
        }
    }

    public void closeDataBase() {
        if (this.mDatabase != null) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }

}
