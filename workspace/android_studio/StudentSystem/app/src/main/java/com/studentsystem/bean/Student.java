package com.studentsystem.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 学生bean
 * 实现Serializable确保可以在页面之间传递
 */
public class Student implements Serializable {

    private String stuId;
    private String password;
    private String name;
    private byte[] avatar;
    private String birthday;
    private String sex;
    private String phone;
    private String email;
    private String hometown;
    private String college;
    private String major;
    private String classIn;

    public String getStuId() {
        return stuId;
    }

    public Student setStuId(String stuId) {
        this.stuId = stuId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Student setAvatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Student setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public Student setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Student setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Student setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getHometown() {
        return hometown;
    }

    public Student setHometown(String hometown) {
        this.hometown = hometown;
        return this;
    }

    public String getCollege() {
        return college;
    }

    public Student setCollege(String college) {
        this.college = college;
        return this;
    }

    public String getMajor() {
        return major;
    }

    public Student setMajor(String major) {
        this.major = major;
        return this;
    }

    public String getClassIn() {
        return classIn;
    }

    public Student setClassIn(String classIn) {
        this.classIn = classIn;
        return this;
    }
}
