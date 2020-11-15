
package com.gxd.gxddb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @说明 数据库表注解 在运行时也有效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE
})
@Inherited
public @interface Table {
    String TableName ();// 表名

    Class TableColumns ();// 表对应的列

    Class DAO ();// 表对应的DAO
}
