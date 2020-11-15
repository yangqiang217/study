package com.gxd.gxddb.test;

import android.content.ContentValues;

import com.gxd.gxddb.BaseDBTable;
import com.gxd.gxddb.dao.BaseDAO;
import com.gxd.gxddb.dao.DAO;
import com.gxd.gxddb.orm.ORMUtil;

import java.util.List;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
public class TestDAO extends BaseDAO {

    public TestDAO (BaseDBTable table) {
        super(table);
    }

    public long insert (TestDataSqlInfo info) {
        ContentValues values = new ContentValues();
        ORMUtil.getInstance().ormInsert(info.getClass(), info, values);
        return mInsert.insert(values);
    }

    public int update (TestDataInfo info) {
        final String taskId = info.getTaskId();
        ContentValues values = new ContentValues(4);
        String whereStr = TestDataColumn.TASK_ID + " = '" + taskId + "'";

        values.put(TestDataColumn.CONTENT, info.getContent());

        return this.update2(whereStr, values);
    }

    public TestDataSqlInfo query(String taskId) {

        String where = TestDataColumn.TASK_ID + " = '" + taskId + "'";

        return mQuery.query(
                null, where, null, null, null, TestDataSqlInfo.class);
    }

    public List<TestDataSqlInfo> queryAll() {
        return mQuery.queryAll(
                null, "", null, TestDataColumn._ID + DAO.ORDER.DESC, null, TestDataSqlInfo.class);
    }

    public void delete (String taskId) {
        String whereString = TestDataColumn.TASK_ID + " = '" + taskId + "'";
        this.delete2(whereString);
    }

    public void deleteAll () {
        this.delete2(null);
    }
}
