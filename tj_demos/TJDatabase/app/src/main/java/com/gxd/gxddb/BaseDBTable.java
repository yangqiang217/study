
package com.gxd.gxddb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import com.gxd.gxddb.dao.DAO;

/**
 * @说明 对象级别的表结构 所有继承于该类的对象必须在类前注入 {@link Table}
 * @说明 在生成一个表对象的时候必须指定建立在这张表上的{@link DAO} 对象保证业务层对象不直接操纵数据库层结构,保证业务层和持久层完全解耦
 */
public abstract class BaseDBTable {

    BaseDB mDatabase = null;

    public BaseDBTable(BaseDB database) {
        mDatabase = database;
    }

    /**
     * @return BaseDB 得到表所在的数据库实例
     */
    public BaseDB getDatabase() {
        return this.mDatabase;
    }

    /**
     * @retrun 得到构建该表的sql语句(由DB在构建表的时候调用)
     */
    public String getCreateTableString() {
        Class clazz = getTableColumns();
        Field[] fields = clazz.getFields();
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append(" CREATE TABLE IF NOT EXISTS ").append(getTableName()).append("(");
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                Column c = f.getAnnotation(Column.class);
                if (c != null) {
                    sbuilder.append(f.get(clazz).toString()).append(" ").append(c.defineType());
                    sbuilder.append(",");
                }
            }
            if (fields.length > 0) {
                sbuilder.deleteCharAt(sbuilder.length() - 1);
            }
        } catch (Exception e) {
        }
        sbuilder.append(");");
        return sbuilder.toString();
    }

    /**
     * @return 得到表名
     */
    public String getTableName() {
        Table annotation = this.getClass().getAnnotation(Table.class);
        return annotation.TableName();
    }

    /**
     * @return 得到对应这张表的DAO
     */
    public DAO getTableDAO() {
        Table annotation = this.getClass().getAnnotation(Table.class);
        Class clazz = annotation.DAO();
        DAO tableDAO = null;
        try {
            Constructor c = clazz.getConstructor(BaseDBTable.class);
            tableDAO = (DAO) c.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableDAO;
    }

    /**
     * @retrun 得到这个表对应的列字段
     */
    public Class getTableColumns() {
        Table annotation = this.getClass().getAnnotation(Table.class);
        return annotation.TableColumns();
    }
}
