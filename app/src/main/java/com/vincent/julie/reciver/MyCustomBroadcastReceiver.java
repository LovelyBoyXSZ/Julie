package com.vincent.julie.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ToastUtils;

/**
 * 用来接收各种广播
 * Created by Vincent on 2016/10/20.
 */

public class MyCustomBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
            MyLog.d("系统广播","连接电源了");//经过测试有用，能收到广播
            ToastUtils.showDefaultToast(MyApplication.getInstance(),"连接电源了");
        }else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            MyLog.d("系统广播","断开");//很奇怪，断开的时候并没有打印日志，等到连接上了才打印日志了，先打印的断开，然后打印连接的日志
        }else if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            MyLog.d("系统广播","网络发生改变");//当前网络发生改变的时候触发此广播
        }else if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
            MyLog.d("系统广播","关机了");//测试通过，关机的时候收到广播了；另外重启设备的时候也能收到此广播
        }else if(intent.getAction().equals("custom_broadcast_receiver")) {
            MyLog.d("自定义广播","收到自定义广播");//收到了自定义广播
            ToastUtils.showDefaultToast(MyApplication.getInstance(),"收到自定义广播");
        }
    }

}
