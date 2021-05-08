package com.studentsystem.event;

import com.studentsystem.bean.Admin;

/**
 * eventbus的event，修改管理员信息后通知别的页面
 */
public class AdminChangedEvent {
    private Admin mAdmin;

    public Admin getAdmin() {
        return mAdmin;
    }

    public AdminChangedEvent setAdmin(Admin admin) {
        mAdmin = admin;
        return this;
    }
}
