package com.gxd.gxddb.test;

import com.gxd.gxddb.BaseDB;
import com.gxd.gxddb.BaseDBTable;
import com.gxd.gxddb.Table;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
@Table(TableName = TestDataTable.TABLE_NAME, // 表名
       TableColumns = TestDataColumn.class,// 表对应的列
       DAO = TestDAO.class)
public class TestDataTable extends BaseDBTable {

    public static final String TABLE_NAME = "tablename";

    public TestDataTable(BaseDB database) {
        super(database);
    }
}
