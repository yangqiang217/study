package com.gxd.gxddb.test;

import com.gxd.gxddb.Column;
import com.gxd.gxddb.DatabaseColumn;
import com.gxd.gxddb.DatabaseTypeConstant;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
public interface TestDataColumn extends DatabaseColumn {

    @Column(defineType = DatabaseTypeConstant.INT + " "
            + DatabaseTypeConstant.PRIMARY)
    String _ID = "_id";

    // 数据库主键
    @Column(defineType = DatabaseTypeConstant.TEXT)
    String TASK_ID = "task_id";

    @Column(defineType = DatabaseTypeConstant.TEXT)
    String CONTENT = "content";
}
