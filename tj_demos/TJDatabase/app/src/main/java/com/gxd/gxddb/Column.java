
package com.gxd.gxddb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @说明 数据库列注解 在运行时也有效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.FIELD
})
public @interface Column {

    String defineType ();// 定义在数据库中的字段类型

}
