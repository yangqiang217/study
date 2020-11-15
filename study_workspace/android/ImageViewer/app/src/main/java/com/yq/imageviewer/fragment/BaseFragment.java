package com.yq.imageviewer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yq.imageviewer.utils.FileUtil;

import butterknife.ButterKnife;

/**
 * Created by yangqiang on 08/02/2018.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(viewLayout(), container, false);
        ButterKnife.bind(this, view);

        initView(view, savedInstanceState);

        return view;
    }

    public abstract int viewLayout();
    public abstract void initView(@NonNull View view, @Nullable Bundle savedInstanceState);

    protected final void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

    protected final void showDialog(String msg, final DialogInterface.OnClickListener onPosLis,
        final DialogInterface.OnClickListener onNegLis) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onPosLis != null) {
                        onPosLis.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onNegLis != null) {
                        onNegLis.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            })
            .create()
            .show();
    }
}
