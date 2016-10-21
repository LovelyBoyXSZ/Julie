package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ToastUtils;
import com.vincent.julie.view.CountDownView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/12 9:22
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.cdv_jishi)
    CountDownView cdvJishi;

    int count=0;

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_setting);

    }

    @Override
    protected void setListener() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        cdvJishi.start();
        cdvJishi.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {
                count++;
                ToastUtils.showDefaultToast(SettingActivity.this,"开始计时,第"+count+"次");
                MyLog.d("count",String.valueOf(count));
            }

            @Override
            public void onFinishCount() {
                ToastUtils.showDefaultToast(SettingActivity.this,"计时结束");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            if(count<5){
                                cdvJishi.start();//结束之后暂停两秒继续开始
                            }else {
                                ToastUtils.showDefaultToast(MyApplication.getInstance(),"行了，效果我看到了，不要运行了");
                                MyLog.d("tag","广告倒计时效果结束");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
