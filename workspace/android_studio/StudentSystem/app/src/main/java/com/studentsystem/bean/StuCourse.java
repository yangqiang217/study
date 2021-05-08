package com.studentsystem.bean;

import java.math.BigDecimal;

/**
 * 学生和课程对应关系
 */
public class StuCourse {

    private String stuId;
    private String courseId;
    private float score;//成绩
    private float credit;//学分

    public String getStuId() {
        return stuId;
    }

    public StuCourse setStuId(String stuId) {
        this.stuId = stuId;
        return this;
    }

    public String getCourseId() {
        return courseId;
    }

    public StuCourse setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public float getScore() {
        return score;
    }

    public StuCourse setScore(float score) {
        this.score = score;
        return this;
    }

    public float getCredit() {
        return credit;
    }

    public StuCourse setCredit(float credit) {
        this.credit = credit;
        return this;
    }

    /**
     * 根据分数计算所得学分
     * 保留一位小数
     * @param score 百分制分数
     * @param totalCredit 课程总学分
     */
    public static float getCreditByScore(float score, float totalCredit) {
        BigDecimal bg = new BigDecimal(totalCredit * score / 100.0);
        return bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
