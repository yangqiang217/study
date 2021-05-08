package com.studentsystem.bean;

/**
 * 我的成绩bean
 * 用在我的成绩页面
 */
public class MyScore {
    private String courseName;
    private String score;
    private float credit;

    public String getCourseName() {
        return courseName;
    }

    public MyScore setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getScore() {
        return score;
    }

    public MyScore setScore(String score) {
        this.score = score;
        return this;
    }

    public float getCredit() {
        return credit;
    }

    public MyScore setCredit(float credit) {
        this.credit = credit;
        return this;
    }
}
