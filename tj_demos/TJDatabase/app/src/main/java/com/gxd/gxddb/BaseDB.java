
package com.gxd.gxddb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import com.gxd.gxddb.dao.DAO;
import com.gxd.gxddb.dao.DAOFactory;

/**
 * @说明 使用说明 1. 继承 {@link BaseDB} 2. 向DB中注册表~表必须继承于{@link BaseDBTable}的继承树 3.
 *     DB实例和表之间的关系通过配置 {@link Database}来实现 关联性注解 (说明当一个数据库建立以后,业务层要完全对数据层解耦,
 *     DAO是实现ORM映射的代理工具,为了实现DAO的管理,统一接口,引入Dao工厂管理)
 */
public abstract class BaseDB extends SQLiteOpenHelper {
    public static final String TAG = "BaseDB";
    /* 注册表 */
    public List<BaseDBTable> mTables = new LinkedList<BaseDBTable>();
    /* 拦截重复注册 */
    public boolean isRegistor = false;
    /* DAO工厂 */
    public DAOFactory mFactory = null;

    public BaseDB(Context context,
            String databaseName,
            int version,
            DAOFactory factory) {
        super(context, databaseName, null, version);
        mFactory = factory;
        if (!this.isRegistor) {
            this.registorTables();
            this.isRegistor = true;
        }
    }

    /**
     * @方法说明 注册表实例
     * @return 将{@link Database}中注册的表信息注册到数据库中
     */
    protected void registorTables() {
        Database database = this.getClass().getAnnotation(Database.class);
        Class[] clazzs = database.tables();
        for (Class clazz : clazzs) {
            try {
                Constructor c = clazz.getConstructor(BaseDB.class);
                BaseDBTable table = (BaseDBTable) c.newInstance(this);
                this.registorTable(table);
            } catch (Exception e) {
            }
        }
    }

    /**
     * @说明 注册一张表到DB中
     * @return 添加表到注册队列,注入DAO工厂DAO类
     */
    protected void registorTable(BaseDBTable table) {
        mTables.add(table);
        DAO dao = table.getTableDAO();
        mFactory.registorDAO(dao.getClass(), dao);
    }

    public void addTable(BaseDBTable table) {
        this.registorTable(table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (BaseDBTable table : mTables) {
            db.execSQL(table.getCreateTableString());// 创建表
        }
    }   
}
