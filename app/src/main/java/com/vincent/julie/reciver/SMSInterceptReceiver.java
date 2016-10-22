package com.vincent.julie.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.NotificationUtils;

/**
 * Created by Vincent on 2016/10/22.
 */

public class SMSInterceptReceiver extends BroadcastReceiver {

    final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    //有新短信时系统发出的广播，有序，可拦截。
    public SMSInterceptReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.v(SMSInterceptReceiver.class.getSimpleName(), ">>>>>>>onReceive start");
        // 第一步、获取短信的内容和发件人
        StringBuilder body = new StringBuilder();// 短信内容
        StringBuilder number = new StringBuilder();// 短信发件人
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] _pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[_pdus.length];
            for (int i = 0; i < _pdus.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
            }
            for (SmsMessage currentMessage : message) {
                body.append(currentMessage.getDisplayMessageBody());
                number.append(currentMessage.getDisplayOriginatingAddress());
            }
            String smsBody = body.toString();
            String smsNumber = number.toString();
            MyLog.d(SMSInterceptReceiver.class.getSimpleName(),"号码："+smsNumber+",内容："+smsBody);
            if (smsNumber.contains("+86")) {
                smsNumber = smsNumber.substring(3);
            }
            // 第二步:确认该短信内容是否满足过滤条件
            boolean flags_filter = false;
            if (smsNumber.equals("18696855784")) {// 屏蔽10086发来的短信
                flags_filter = true;
                MyLog.d(SMSInterceptReceiver.class.getSimpleName(), "拦截到18290258163信息");
                NotificationUtils.sendNotification(MyApplication.getInstance(),"com.vincent.julie.ui.activity.SendMsgActivity",100,smsNumber,smsBody);
            }
            // 第三步:取消 取消不了，系统还是能收到
            if (flags_filter) {
                this.abortBroadcast();//终止广播，不让系统
            }
        }
        MyLog.d(SMSInterceptReceiver.class.getSimpleName(), ">>>>>>>onReceive end");
    }
}


