package com.vincent.julie.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.listener.SendMsgOnClickListener;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;
import com.vincent.julie.view.AnFQNumEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Vincent on 2016/10/21.
 */

@RuntimePermissions
public class SendMsgActivity extends BaseActivity {

    private static final String TAG=SendMsgActivity.class.getSimpleName();
    private String SENT_SMS_ACTION = "SENT_SMS_ACTION";	//发送状态回执广播的Action
    private String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"; //接受状态回执广播的Action

    @BindView(R.id.tv_custom)
    AnFQNumEditText tvCustom;


    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {//ps：使用注解之后再下面操作对象会报空指针错误
        setContentView(R.layout.act_send_msg);
        setTitleText("短信操作相关");
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        tvCustom.setEtHint("内容")//设置提示文字
                .setEtMinHeight(200)//设置最小高度，单位px
                .setLength(50)//设置总字数
                .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
                .setLineColor("#3F51B5")//设置横线颜色
                .show();
        tvCustom.setText("发送短信");
        tvCustom.setSendMsgListener(new SendMsgOnClickListener() {

            /**
             * 定义接口获取发送按钮的点击事件
             * @param msgConent 输入的内容
             */
            @Override
            public void SendMsg(String msgConent) {
//                ToastUtils.showSingleTextToast(MyApplication.getInstance(),"操作数据："+msgConent);
//                SendSMS(msgConent);
                SendMsgActivityPermissionsDispatcher.sendSMSWithCheck(SendMsgActivity.this,msgConent);
            }
        });

        // 注册广播
        registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
        registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));

    }

    /**
     * 发送短信
     * @param msgConent
     */
    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void sendSMS(String msgConent) {
        send(msgConent);
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void showRationaleForSendSMS(PermissionRequest request) {
        showRationaleDialog("发送短信必须动态授权", request);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void sendSMSDenied() {
        ToastUtils.showDefaultToast(getApplicationContext(), "您拒绝了授予权限，无法发送");
    }

    //拒绝权限不再弹出权限提示框
    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})
    void sendSMSNeverAskAgain() {
        ToastUtils.showDefaultToast(getApplicationContext(), "不再允许询问该权限，该功能不可用");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SendMsgActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //弹出自定义权限提示框
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 具体实施发送短信
     * @param msgConent
     */
    private void send(String msgConent) {
//        ToastUtils.showSingleTextToast(MyApplication.getInstance(),"就不发了，短信操作不写这里了");
        View view= LayoutInflater.from(this).inflate(R.layout.dlg_input_phone_num,null);
        ImageView ivClear=(ImageView)view.findViewById(R.id.iv_clear_all);
        EditText etPhoneNum=(EditText)view.findViewById(R.id.et_input_phone);
        //TODO 自动弹出软键盘
        SystemUtilts.getInputKeyboard(etPhoneNum);
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhoneNum.getText().length() == start && start == 0) {
                    ivClear.setVisibility(View.GONE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etPhoneNum.addTextChangedListener(watcher);

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNum.setText("");
            }
        });
        new AlertDialog.Builder(this)
            .setTitle("请输入手机号码")
                .setView(view)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etPhoneNum.getText().toString().equals("")){
                            ToastUtils.showSingleTextToast(MyApplication.getInstance(),"请输入号码");
                        }else {
                            sendMsg(etPhoneNum.getText().toString().trim(),msgConent);
                        }
                    }
                })
                .setNegativeButton("取消发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
    /**
     * 调用此方法发送短信
     *
     * @param phoneNum
     * @param addressInfo
     */
    public  void sendMsg(String phoneNum, String addressInfo) {
        try {
            SmsManager smsManager = SmsManager.getDefault();//初始化消息实例

            // 与回执意图所绑定的广播接收者,即发送以后触发,向广播发送一条广播信息
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            // 新建发送状态回执意图
            PendingIntent sentPI = PendingIntent.getBroadcast(MyApplication.getInstance(), 0, sentIntent,
                    0);

            // 与回执意图所绑定的广播接收者,即接收以后触发,向广播发送一条广播信息
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            // 新建接收状态回执意图
            PendingIntent deliverPI = PendingIntent.getBroadcast(MyApplication.getInstance(), 0,
                    deliverIntent, 0);

            if (addressInfo.length() > 70) {
                Log.d(TAG,"addressInfo.length="+addressInfo.length());
                ArrayList<String> texts = smsManager.divideMessage(addressInfo);//自动拆分短信
                for (String text : texts) {
                    smsManager.sendTextMessage(
                            phoneNum,//目标电话号码
                            null,//短信中心电话号码为null时，使用系统默认
                            text,//短信内容
                            sentPI,//sentIntent,发送状态
                            deliverPI//对方接受状态
                    );
                }
            } else {
                Log.d(TAG,"addressInfo.length="+addressInfo.length());
                smsManager.sendTextMessage(
                        phoneNum,//目标电话号码
                        null,//短信中心电话号码为null时，使用系统默认
                        addressInfo,//短信内容
                        sentPI,//sentIntent,发送状态
                        deliverPI//对方接受状态
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showDefaultToast(MyApplication.getInstance(),"发送失败");
        }
    }
    /**
     * 短信发送状态的广播接收者
     */
    private BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
//                    recordAfterSendSms(sendPhoneNumber, sendMsg);	//短信发送成功时才在"已发送"里记录
                    Log.i(TAG, "短信发送成功");
                    ToastUtils.showDefaultToast(getApplicationContext(),"短信发送成功");
                    tvCustom.clearText();
                    break;
                default:
                    ToastUtils.showDefaultToast(getApplicationContext(),"短信发送失败");
                    Log.i(TAG, "短信发送失败");
                    break;
            }
        }
    };

    /**
     * 短信接收状态的广播接收者
     *
     * <p>
     * 接收状态一直没有试出来,有待完善,哪位大哥整出来这个了还请指教下
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //表示对方成功收到短信
            Log.i(TAG, "对方接收成功");
            ToastUtils.showDefaultToast(getApplicationContext(),"对方已接受");
        }
    };
}
