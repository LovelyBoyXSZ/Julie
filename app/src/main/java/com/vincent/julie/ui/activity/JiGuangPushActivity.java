package com.vincent.julie.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ExampleUtil;
import com.vincent.julie.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/18 16:22
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class JiGuangPushActivity extends InstrumentedActivity {

    public static boolean isForeground = false;
    @BindView(R.id.btn_get_registration_id)
    Button btnGetRegistrationId;
    @BindView(R.id.tv_show_registration_id)
    TextView tvShowRegistrationId;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==1){
                btnGetRegistrationId.setText("收到handler发送的消息！！");
                tvShowRegistrationId.setText("我就更新下个视图，没什么事儿..");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.act_jiguang_push);
        JPushInterface.init(this);
        JPushInterface.resumePush(this);
//        JPushInterface.getStringTags()
        MyLog.d("RegistrationID", JPushInterface.getRegistrationID(MyApplication.getInstance()));
        MyLog.d("JpushInterface","init()和resumePush()");
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }



    @OnClick(R.id.btn_get_registration_id)
    public void onClick() {
        if(ExampleUtil.isConnected(this)){
            MyLog.d("已连接");
            MyLog.d("APP Key",ExampleUtil.getAppKey(this));
        }else {
            MyLog.d("未连接");
        }
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {
            tvShowRegistrationId.setText("RegId:" + rid);
            MyLog.d("registrationId",rid);
        } else {
            MyLog.d("It's error for get registration...");
            ToastUtils.showSingleTextToast(MyApplication.getInstance(),"Get registration fail, JPush init failed!");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyLog.d("Thead","newThread");
                try {
                    Thread.sleep(3000);
                    Message message=Message.obtain();
                    message.arg1=1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
