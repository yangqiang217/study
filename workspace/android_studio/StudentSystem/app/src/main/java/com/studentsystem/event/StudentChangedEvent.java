package com.studentsystem.event;

import com.studentsystem.bean.Student;

/**
 * eventbus的event，修改学生后通知别的页面
 */
public class StudentChangedEvent {

    //具体做了什么
    public static final int ACTION_ADD = 2001;
    public static final int ACTION_EDIT = 2002;
    public static final int ACTION_DELETE = 2003;

    /**
     * 取值上面几个常量
     */
    private int mAction;
    private Student mStudent;

    public StudentChangedEvent(int action, Student student) {
        mAction = action;
        mStudent = student;
    }

    public int getAction() {
        return mAction;
    }

    public Student getStudent() {
        return mStudent;
    }
}
