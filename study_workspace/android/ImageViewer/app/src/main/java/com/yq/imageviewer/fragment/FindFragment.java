package com.yq.imageviewer.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.R;
import com.yq.imageviewer.activity.StatusActivity;
import com.yq.imageviewer.event.RefreshEvent;
import com.yq.imageviewer.utils.DeviceUtils;
import com.yq.imageviewer.utils.FileUtil;
import com.yq.imageviewer.view.TitleAndDateView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {


    @BindView(R.id.frag_setting_container) ViewGroup mContainer;

    @Override
    public int viewLayout() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContainer.setPadding(0, DeviceUtils.getStatusBarHeight(getContext()), 0, 0);
    }

    @OnClick(R.id.frag_setting_btn_purge)
    public void onPurgeClick() {
        showDialog("Confirm purge the un-encrypted folders?",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast(FileUtil.purge(Const.PATH) + " dirs was pursed");
                }
            }, null
        );
    }

    @OnClick(R.id.frag_setting_btn_downloaded)
    public void onDownloadedClick() {
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // 获取布局
        final View dialogView = View.inflate(getContext(), R.layout.dialog_downloaded_message, null);
        // 获取布局中的控件
        final TitleAndDateView titleAndDateView = dialogView.findViewById(R.id.dlg_dld_titleanddate);
        final Button button = dialogView.findViewById(R.id.dlg_dld_btn);
        // 设置参数
        builder.setView(dialogView);

        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String titleAndDate = titleAndDateView.tryGetFormatedStr();
                if (!TextUtils.isEmpty(titleAndDate)) {
                    FileUtil.renameFileEnd(
                        FileUtil.renameDirToEncryped(
                            FileUtil.getDownloadedDirFromTemp(), titleAndDate));

                    alertDialog.dismiss();
                    showToast("downloaded file convertAllToEncrypt success");
                }
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.frag_setting_btn_sync_et)
    public void onSyncFromInputClick() {
        StatusActivity.start(this, StatusActivity.Status.SYNC_INPUT);
    }

    @OnClick(R.id.frag_setting_btn_convert_to_encrypted)
    public void onConvertClick() {
        showDialog("format exp: XX | YYYY_2018-05-21",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int count = FileUtil.convertAllToEncrypt(Const.PATH);
                    showToast("convertAllToEncrypt finish, " + count + " were converted");
                    EventBus.getDefault().post(new RefreshEvent());
                }
            }, null
        );
    }

    @OnClick(R.id.frag_setting_btn_convert_to_unencrypted)
    public void onConvertToUnEncryptedClick() {
        showDialog("confirm convert all to unencrypted?",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int count = FileUtil.convertAllToUnEncrypt(Const.PATH);
                    showToast("convertAllToUnEncrypt finish, " + count + " were converted");
                }
            }, null
        );
    }

    @OnClick(R.id.frag_setting_btn_show_filename)
    public void onRealNameEncryptedName() {
        StatusActivity.start(this, StatusActivity.Status.REALNAME_ENCRYPTEDNAME);
    }
}
