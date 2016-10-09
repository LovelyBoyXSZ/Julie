package com.vincent.julie.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.Contants;
import com.vincent.julie.ui.fragment.MainFragment;
import com.vincent.julie.util.SharedPreferencesUtil;
import com.vincent.julie.util.TimeCount;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 2016/9/26.
 */

public class WelcomActivity extends BaseActivity {
    private static final int NUM = 1;
    @BindView(R.id.tv_go_main)
    TextView tvGoMain;

    private SharedPreferencesUtil shared;
    private boolean isFristInstall;
    private int recLen=2;
    private Timer timer;


    @Override
    protected void findViewById() {
        setContentView(R.layout.act_welcom);
        hideTitle();
        shared=new SharedPreferencesUtil(WelcomActivity.this, Contants.WebUrl.CONFIG);
        isFristInstall=shared.getBoolean(Contants.FIRST_START);
        timer=new Timer();
        timer.schedule(task, 1000, 1000);       // timeTask

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    tvGoMain.setText(""+recLen+"s");
                    if(recLen == 0){
                        timer.cancel();
                        if(!isFristInstall){
                            goGuide();
                        }else {
//                            goGuide();//test guide
                            goMain();
                        }
                    }
                }
            });
        }
    };
    /**
      *跳转到主页
      *@author Vincent QQ1032006226
      *created at 2016/9/26 16:47
      */
    private void goMain() {
        startActivity(new Intent(WelcomActivity.this,MainActivity.class));
        finish();
    }

    /**
  *第一次安装跳转到去引导
  *@author Vincent QQ1032006226
  *created at 2016/9/26 16:46
  */
    private void goGuide() {
        startActivity(new Intent(WelcomActivity.this,GuideActivity.class));
        SharedPreferencesUtil shared=new SharedPreferencesUtil(WelcomActivity.this,Contants.WebUrl.CONFIG);
        shared.putBoolean(Contants.FIRST_START,true);
        finish();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        //将屏幕设置为全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick(R.id.tv_go_main)
    public void onClick() {
        startActivity(new Intent(WelcomActivity.this,MainActivity.class));
        finish();
    }
}
