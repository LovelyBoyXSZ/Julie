package com.vincent.julie.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.vincent.julie.app.BaseActivity;


/**
 *此Activity的作用就是提升用户点击app的响应速度
 *@author Vincent
 *created at 2016/9/26 8:34
 */
public class SecondsOpenAppActivity extends BaseActivity{
    private long endTime=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SecondsOpenAppActivity.this,WelcomActivity.class));
        finish();
    }

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        endTime=System.currentTimeMillis();
    }
}
