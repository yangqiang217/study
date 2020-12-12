package com.example.qiangyang.dialogfragment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private MyDialog myDialog;
    private EditText editText;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.etMain);
        container = (RelativeLayout) findViewById(R.id.container);

        myDialog = new MyDialog();
        myDialog.setOnDismissListener(new MyDialog.OnDismissListener() {
            @Override
            public void onDismiss(String content) {
                editText.setText(content);
                editText.requestFocus();
                hideKeyboard();
                editText.setSelection(content.length());
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show(getSupportFragmentManager(), "tag");
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onclick");
                hideKeyboard();
                container.requestFocus();
                myDialog.show(getSupportFragmentManager(), "tag");
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
