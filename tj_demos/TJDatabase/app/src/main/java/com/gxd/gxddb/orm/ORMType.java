
package com.gxd.gxddb.orm;

/**
 * @说明 ORM类型
 */
public interface ORMType {

    int NORMAL_COLUMN = 0;// 基本类型
    int ITERATOR = 1;// 迭代模式
    int CLASS = 2;// 对象集合模式
}
