package com.gxd.gxddb.test;

import com.gxd.gxddb.orm.ORM;

import java.io.Serializable;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
public class TestDataSqlInfo implements Serializable {

    @ORM(mappingColumn = TestDataColumn.TASK_ID)
    public String taskId; // 领取任务ID,数据库主键
    @ORM(mappingColumn = TestDataColumn.CONTENT)
    public String content; // 用户名ID
}
