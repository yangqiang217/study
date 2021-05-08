package com.studentsystem.listener;

import com.studentsystem.bean.Course;

/**
 * 课程列表每个item的点击事件
 */
public interface OnCourseItemClickListener {
    void onCourseItemClick(int position, Course course);
}
