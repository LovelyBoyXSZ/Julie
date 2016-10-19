package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.vincent.julie.logs.MyLog;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：此Activity用来保证后台不被杀死
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/19 14:49
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class KeepLiveActivity extends AppCompatActivity {

    private final static String TAG="KeppLive";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d(TAG,"LiveActivity->onCreate()");

        Window window=getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        WindowManager.LayoutParams params=window.getAttributes();
        params.x=0;
        params.y=0;
        params.height=1;
        params.width=1;
        window.setAttributes(params);
        


    }
}
