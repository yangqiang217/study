
package com.gxd.gxddb.sql;

import com.gxd.gxddb.dao.BaseDAO;

/**
 * @说明 sql语句接口
 * @目的 为了屏蔽高级编程语言和关系型数据库之间的语言不耦合, 以及横向拦截，提供对象式的数据库策略即操纵对象即操纵数据库
 *     这个对象位于DAO层和数据库层之间 Sql提供了增删改查四种策略的实现
 */
public abstract class Sql {

    public BaseDAO mDao = null;

    public Sql(BaseDAO dao) {
        mDao = dao;
    }

}
