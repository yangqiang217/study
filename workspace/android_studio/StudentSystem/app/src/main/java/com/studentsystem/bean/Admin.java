package com.studentsystem.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 管理员bean
 * 实现Serializable确保可以在页面之间传递
 */
public class Admin implements Serializable {

    private String adminId;
    private String password;
    private String name;
    private byte[] avatar;
    private String birthday;
    private String sex;
    private String phone;
    private String email;
    private String hometown;
    private String college;//所属学院

    public String getAdminId() {
        return adminId;
    }

    public Admin setAdminId(String adminId) {
        this.adminId = adminId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Admin setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public Admin setName(String name) {
        this.name = name;
        return this;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Admin setAvatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Admin setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public Admin setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Admin setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Admin setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getHometown() {
        return hometown;
    }

    public Admin setHometown(String hometown) {
        this.hometown = hometown;
        return this;
    }

    public String getCollege() {
        return college;
    }

    public Admin setCollege(String college) {
        this.college = college;
        return this;
    }
}
