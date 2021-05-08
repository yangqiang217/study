package com.studentsystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsystem.R;
import com.studentsystem.activity.PersonDetailActivity;
import com.studentsystem.activity.StudentAddActivity;
import com.studentsystem.adapter.StudentListAdapter;
import com.studentsystem.bean.Student;
import com.studentsystem.consts.Consts;
import com.studentsystem.db.DBManager;
import com.studentsystem.event.StudentChangedEvent;
import com.studentsystem.listener.OnStudentItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 底部学生tab
 * 只有管理员才能看到
 */
public class StudentFragment extends Fragment implements View.OnClickListener, OnStudentItemClickListener {

    private EditText mEtSearch;
    private RecyclerView mRvStuList;

    private StudentListAdapter mStudentListAdapter;
    //跳转过去的位置，可能还会继续进编辑，记着位置编辑了好改变列表数据
    private int mJumpedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student, container, false);

        EventBus.getDefault().register(this);

        mEtSearch = view.findViewById(R.id.et_search_input);
        mRvStuList = view.findViewById(R.id.rv_student_list);
        
        view.findViewById(R.id.tv_student_add).setOnClickListener(this);
        view.findViewById(R.id.tv_search_do).setOnClickListener(this);

        mEtSearch.setHint("请输入学生姓名关键字");

        initRecyclerView();
        initData();

        return view;
    }

    //初始化列表ui
    private void initRecyclerView() {
        mStudentListAdapter = new StudentListAdapter(getContext(), false);
        mStudentListAdapter.setOnStudentItemClickListener(this);
        mRvStuList.setAdapter(mStudentListAdapter);
        mRvStuList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        if (Consts.IS_LOCAL) {
            //去本地数据库查
            List<Student> courseList = DBManager.getInstance().getStudentDao().queryDataList();
            mStudentListAdapter.setDataList(courseList);
        }
    }


    private void doSearch() {
        String keyWord = mEtSearch.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            initData();
            return;
        }
        if (Consts.IS_LOCAL) {
            List<Student> queryRes = DBManager.getInstance().getStudentDao().queryDataListByName(keyWord);
            mStudentListAdapter.setDataList(queryRes);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_do:
                doSearch();
                break;
            case R.id.tv_student_add:
                //管理员进入学生添加页面
                Intent intent = new Intent(getActivity(), StudentAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStudentItemClick(int position, Student student) {
        //管理员跳转学生编辑，需要知道编辑结果，会用EventBus通知
        mJumpedPosition = position;
        PersonDetailActivity.start(getContext(), student);
    }

    @Override
    public void onAssignScoreClick(int position, Student student) {}

    /**
     * 接收eventbus事件，学生信息被修改后改变列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveStudentChangeEvent(StudentChangedEvent event) {
        switch (event.getAction()) {
            case StudentChangedEvent.ACTION_ADD:
                mStudentListAdapter.insertItem(event.getStudent());
                mRvStuList.scrollToPosition(0);
                break;
            case StudentChangedEvent.ACTION_EDIT:
                mStudentListAdapter.changeItem(mJumpedPosition, event.getStudent());
                break;
            case StudentChangedEvent.ACTION_DELETE:
                mStudentListAdapter.deleteItem(mJumpedPosition);
                break;
        }
        mJumpedPosition = -1;//修改了后还原
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //必须取消注册eventbus
        EventBus.getDefault().unregister(this);
    }
}
