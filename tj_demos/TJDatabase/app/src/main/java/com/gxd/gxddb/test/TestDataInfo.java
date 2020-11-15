package com.gxd.gxddb.test;

/**
 * Created by v-yangqiang on 2016/5/30.
 */
public class TestDataInfo {

    private String taskId;
    private String content;

    public TestDataInfo (String taskId, String content) {
        this.taskId = taskId;
        this.content = content;
    }

    public TestDataInfo (TestDataSqlInfo sqlInfo) {
        this.taskId = sqlInfo.taskId;
        this.content = sqlInfo.content;
    }

    public TestDataSqlInfo generateSqlInfo () {
        TestDataSqlInfo sqlInfo = new TestDataSqlInfo();
        sqlInfo.taskId = this.taskId;
        sqlInfo.content = this.content;

        return sqlInfo;
    }

    public String getTaskId () {
        return taskId;
    }

    public String getContent () {
        return content;
    }

    @Override
    public String toString () {
        return "taskId:" + taskId + ", content:" + content;
    }
}
