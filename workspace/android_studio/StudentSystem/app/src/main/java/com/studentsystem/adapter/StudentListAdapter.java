package com.studentsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.bean.Student;
import com.studentsystem.fragment.StudentFragment;
import com.studentsystem.listener.OnCourseItemClickListener;
import com.studentsystem.listener.OnStudentItemClickListener;
import com.studentsystem.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程列表adapter，好几个页面都涉及到课程列表，UI可能有微小差异，所以公用一个
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private Context mContext;
    private List<Student> mDataList = new ArrayList<>();
    private OnStudentItemClickListener mOnStudentItemClickListener;

    private boolean mShowAssignScoreBtn;

    public StudentListAdapter(Context ctx, boolean showAssignScoreBtn) {
        mContext = ctx;
        mShowAssignScoreBtn = showAssignScoreBtn;
    }

    public void insertItem(Student student) {
        mDataList.add(0, student);
        notifyItemInserted(0);
    }

    public void changeItem(int position, Student student) {
        mDataList.set(position, student);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void setDataList(List<Student> newData) {
        mDataList.clear();
        mDataList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {
        final Student stu = mDataList.get(position);

        if (mShowAssignScoreBtn) {
            holder.mTvAssignScore.setVisibility(View.VISIBLE);
            //UI要稍作调整
            RelativeLayout.LayoutParams lpSex = (RelativeLayout.LayoutParams) holder.mTvSex.getLayoutParams();
            lpSex.rightMargin = AppUtil.dpToPx(mContext, 120);//数值是根据xml里计算的
            holder.mTvSex.setLayoutParams(lpSex);

            RelativeLayout.LayoutParams lpMajor = (RelativeLayout.LayoutParams) holder.mTvMajor.getLayoutParams();
            lpMajor.rightMargin = AppUtil.dpToPx(mContext, 120);//数值是根据xml里计算的
            holder.mTvMajor.setLayoutParams(lpMajor);
        } else {
            holder.mTvAssignScore.setVisibility(View.GONE);
            //UI要稍作调整
            RelativeLayout.LayoutParams lpSex = (RelativeLayout.LayoutParams) holder.mTvSex.getLayoutParams();
            lpSex.rightMargin = AppUtil.dpToPx(mContext, 20);//数值是根据xml里计算的
            holder.mTvSex.setLayoutParams(lpSex);

            RelativeLayout.LayoutParams lpMajor = (RelativeLayout.LayoutParams) holder.mTvMajor.getLayoutParams();
            lpMajor.rightMargin = AppUtil.dpToPx(mContext, 20);//数值是根据xml里计算的
            holder.mTvMajor.setLayoutParams(lpMajor);
        }

        //用网络请求回来的数据填充到view上
        holder.mTvName.setText(stu.getName());
        holder.mTvCollege.setText(stu.getCollege());
        holder.mTvSex.setText(stu.getSex());
        holder.mTvMajor.setText(stu.getMajor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStudentItemClickListener != null) {
                    mOnStudentItemClickListener.onStudentItemClick(position, stu);
                }
            }
        });

        holder.mTvAssignScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStudentItemClickListener != null) {
                    mOnStudentItemClickListener.onAssignScoreClick(position, stu);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnStudentItemClickListener(OnStudentItemClickListener onStudentItemClickListener) {
        mOnStudentItemClickListener = onStudentItemClickListener;
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView mTvName, mTvCollege, mTvSex, mTvMajor, mTvAssignScore;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_item_stu_name);
            mTvCollege = itemView.findViewById(R.id.tv_item_stu_college);
            mTvSex = itemView.findViewById(R.id.tv_item_stu_sex);
            mTvMajor = itemView.findViewById(R.id.tv_item_stu_major);
            mTvAssignScore = itemView.findViewById(R.id.tv_item_stu_assign_score);
        }
    }
}