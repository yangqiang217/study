package com.studentsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsystem.R;
import com.studentsystem.bean.Course;
import com.studentsystem.bean.StuCourse;
import com.studentsystem.listener.OnCourseItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程列表adapter，好几个页面都涉及到课程列表，UI可能有微小差异，所以公用一个
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {

    private Context mContext;
    private List<Course> mDataList = new ArrayList<>();
    //之前选过的，进来要打勾
    private List<String> mChosenBeforeIdList = new ArrayList<>();

    //已经勾选的课程，用来插入数据库
    private List<Course> mChosenList = new ArrayList<>();
    //已经取消勾选的课程，用来在数据库中移除
    private List<Course> mUnChosenList = new ArrayList<>();

    //是否显示选择的方框，只有选课页面才显示
    private boolean mShowChooseCheckBox;

    private OnCourseItemClickListener mOnItemClickListener;

    public CourseListAdapter(Context ctx, boolean showChoose) {
        mContext = ctx;
        mShowChooseCheckBox = showChoose;
    }

    public void setDataList(List<Course> newData) {
        mDataList.clear();
        mDataList.addAll(newData);
        notifyDataSetChanged();
    }

    public void insertItem(int position, Course course) {
        mDataList.add(position, course);
        notifyItemInserted(position);
    }

    public void changeItem(int position, Course course) {
        mDataList.set(position, course);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 选课页面用，把之前选的也传过来，选过的默认就打勾
     */
    public void setDataList(List<Course> newData, List<StuCourse> chosenBefore) {
        mDataList.clear();
        mDataList.addAll(newData);

        mChosenBeforeIdList.clear();
        //只需要id就行了，也好查找
        for (StuCourse stuCourse : chosenBefore) {
            mChosenBeforeIdList.add(stuCourse.getCourseId());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, final int position) {
        final Course course = mDataList.get(position);

        //用网络请求回来的数据填充到view上
        holder.mTvCourseName.setText(course.getName());
        holder.mTvCredit.setText(course.getCredit() + "学分");
        holder.mTvTotalTime.setText(course.getTotalTime() + "课时");
        holder.mTvObligatory.setText(course.getObligatory());

        if (mShowChooseCheckBox) {
            holder.mCbIsChoose.setVisibility(View.VISIBLE);
        }
        if (mChosenBeforeIdList.contains(course.getCourseId())) {
            holder.mCbIsChoose.setChecked(true);
        } else {
            holder.mCbIsChoose.setChecked(false);
        }

        holder.mCbIsChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //勾选之后把已经选择的Course放入list，方便使用
                if (isChecked) {
                    //如果mUnChosenList里有，说明之前取消过，再勾选就表示没变
                    if (mUnChosenList.contains(course)) {
                        mUnChosenList.remove(course);
                    } else {
                        mChosenList.add(course);
                    }
                } else {
                    if (mChosenList.contains(course)) {
                        mChosenList.remove(course);
                    } else {
                        mUnChosenList.add(course);
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onCourseItemClick(position, course);
                }
            }
        });

//            //调整每个item高度
//            ViewGroup.LayoutParams lpCover = holder.rlCoverContaienr.getLayoutParams();
//            lpCover.height = ScreenUtil.getScreenWidth(context) / 3;
    }

    public List<Course> getChosenList() {
        return mChosenList;
    }
    public List<Course> getUnChosenList() {
        return mUnChosenList;
    }
    public void clearChosenList() {
        mChosenList.clear();
    }
    public void clearUnChosenList() {
        mUnChosenList.clear();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnItemClickListener(OnCourseItemClickListener onClickListener) {
        mOnItemClickListener = onClickListener;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView mTvCourseName, mTvCredit, mTvTotalTime, mTvObligatory;
        CheckBox mCbIsChoose;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCourseName = itemView.findViewById(R.id.tv_item_course_name);
            mTvCredit = itemView.findViewById(R.id.tv_item_course_credit);
            mTvTotalTime = itemView.findViewById(R.id.tv_item_course_totaltime);
            mTvObligatory = itemView.findViewById(R.id.tv_item_course_obligatory);
            mCbIsChoose = itemView.findViewById(R.id.cb_item_course_ischoose);
        }
    }
}