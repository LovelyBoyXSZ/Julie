package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
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
                ToastUtils.showDefaultToast(SettingActivity.this,"开始计时");
            }

            @Override
            public void onFinishCount() {
                ToastUtils.showDefaultToast(SettingActivity.this,"计时结束");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            cdvJishi.start();//结束之后暂停两秒继续开始
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
