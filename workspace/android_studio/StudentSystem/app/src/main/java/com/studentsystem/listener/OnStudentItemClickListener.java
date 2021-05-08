package com.studentsystem.listener;

import com.studentsystem.bean.Student;

/**
 * 学生列表每个item的点击事件
 */
public interface OnStudentItemClickListener {
    void onStudentItemClick(int position, Student student);
    void onAssignScoreClick(int position, Student student);
}
