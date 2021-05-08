package com.studentsystem.bean;

import java.io.Serializable;

/**
 * 课程bean
 * 实现Serializable确保可以在页面之间传递
 */
public class Course implements Serializable {

    private String courseId;
    private String name;
    private int totalTime;//课时
    private float credit;//学分
    private String teacher;//授课教师姓名（这里就不做那么复杂不用教师id了，因为正常情况下教师不一定注册了所以不一定有id）
    private String obligatory;//必修/选修

    public String getCourseId() {
        return courseId;
    }

    public Course setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public Course setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public float getCredit() {
        return credit;
    }

    public Course setCredit(float credit) {
        this.credit = credit;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    public Course setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String getObligatory() {
        return obligatory;
    }

    public Course setObligatory(String obligatory) {
        this.obligatory = obligatory;
        return this;
    }
}
