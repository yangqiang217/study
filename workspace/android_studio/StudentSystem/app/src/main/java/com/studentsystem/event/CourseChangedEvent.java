package com.studentsystem.event;

import com.studentsystem.bean.Course;

/**
 * eventbus的event，修改课程后通知别的页面
 */
public class CourseChangedEvent {

    //具体做了什么
    public static final int ACTION_ADD = 2001;
    public static final int ACTION_EDIT = 2002;
    public static final int ACTION_DELETE = 2003;

    /**
     * 取值上面几个常量
     */
    private int mAction;
    private Course mCourse;

    public CourseChangedEvent(int action, Course course) {
        mAction = action;
        mCourse = course;
    }

    public int getAction() {
        return mAction;
    }

    public Course getCourse() {
        return mCourse;
    }
}
