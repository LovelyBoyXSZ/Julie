package com.vincent.julie.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;

/**
 * 注意权限问题
 * Created by Vincent on 2016/10/22.
 */

public class CallPhoneInterceptReceiver extends BroadcastReceiver {

    private static TelephonyManager tpm;
    private String phoneNumber;
    private String  data;

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取电话通讯服务
        tpm = (TelephonyManager) MyApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);
        //创建一个监听对象，监听电话状态改变事件
        tpm.listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
        intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        data=getResultData();
        Bundle dat= getResultExtras(true);
        if(data.equals("")){//如果是空号码，就挂断
            setResultData(null);//挂断电话
        }
        //如果是去电
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
//            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            MyLog.d("手机状态", "call OUT:" + phoneNumber);
        }else{
            //非去电即来电
//            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            MyLog.d("手机状态", "call OUT:" + phoneNumber);
        }


    }
   static class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: //空闲
                    MyLog.d(CallPhoneInterceptReceiver.class.getSimpleName(),"空闲状态");
                    break;
                case TelephonyManager.CALL_STATE_RINGING: //来电
                    MyLog.w(CallPhoneInterceptReceiver.class.getSimpleName(),"有人打电话来了:"+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: //摘机（正在通话中）
                    MyLog.w(CallPhoneInterceptReceiver.class.getSimpleName(),"正在通话中:"+incomingNumber);
                    break;
            }
        }

    }
}




