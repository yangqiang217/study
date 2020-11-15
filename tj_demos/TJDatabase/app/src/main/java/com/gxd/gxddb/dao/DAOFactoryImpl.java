
package com.gxd.gxddb.dao;

import java.util.HashMap;
import java.util.Map;


/**
 * DAO 工厂
 */
public final class DAOFactoryImpl implements DAOFactory {

    private static DAOFactoryImpl sInstance = new DAOFactoryImpl();
    private Map<String, DAO> mDAOs = new HashMap<String, DAO>();
    private Map<Class, DAO> mDAOTables = new HashMap<Class, DAO>();

    private DAOFactoryImpl() {
    }

    public static DAOFactoryImpl getInstance() {
        return sInstance;
    }

    // public void registorDAO(String tableName,DAO dao){
    // mDAOs.put(tableName, dao);
    // }
    // public DAO buildDAO(String tableName){
    // return mDAOs.get(tableName);
    // }
    public void registorDAO(Class clazz, DAO dao) {
        this.mDAOTables.put(clazz, dao);
    }

    public <T> T buildDAO(Class<T> clazz) {
        return (T) mDAOTables.get(clazz);
    }
}
