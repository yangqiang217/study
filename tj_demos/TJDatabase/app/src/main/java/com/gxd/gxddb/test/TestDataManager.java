package com.gxd.gxddb.test;

import com.gxd.gxddb.dao.DAOFactoryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
public class TestDataManager {

    private static TestDataManager sInstance = new TestDataManager();
    private final TestDAO testDAO;

    private TestDataManager () {
        testDAO = DAOFactoryImpl.getInstance().buildDAO(
                TestDAO.class);
    }

    public static TestDataManager getInstance () {
        return sInstance;
    }

    /**
     * 插入单条领取任务，数据库
     */
    public void insert (TestDataInfo info) {
        testDAO.insert(info.generateSqlInfo());
    }

    public boolean update (TestDataInfo info) {
        testDAO.update(info);
        return true;
    }

    public TestDataInfo query (final String taskId) {
        TestDataSqlInfo sqlInfo = testDAO.query(taskId);

        if (null == sqlInfo) {
            return null;
        }

        return new TestDataInfo(sqlInfo);
    }

    public ArrayList<TestDataInfo> queryAll () {
        ArrayList<TestDataInfo> arrayTask = new ArrayList<TestDataInfo>();
        List<TestDataSqlInfo> arraySql = testDAO.queryAll();

        if (null == arraySql) {
            return arrayTask;
        }

        for (TestDataSqlInfo sqlInfo : arraySql) {
            arrayTask.add(new TestDataInfo(sqlInfo));
        }

        return arrayTask;
    }

    public void delete (final String taskId) {
        testDAO.delete(taskId);
    }

    public void deleteAll () {
        testDAO.deleteAll();
    }
}
