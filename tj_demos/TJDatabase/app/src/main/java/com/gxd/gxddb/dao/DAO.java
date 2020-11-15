
package com.gxd.gxddb.dao;

public interface DAO {

    interface REST_COMMAND {
        int ADD = 0;
        int DELETE = 1;
        int UPDATE = 2;
        int QUERY = 3;
        int BATCH_ADD = 4;
    }

    interface ORDER {
        String DESC = " desc";// 降序
        String ASC = " ";// 升序
    }
}
