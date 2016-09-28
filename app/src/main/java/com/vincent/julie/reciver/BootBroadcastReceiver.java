package com.vincent.julie.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vincent.julie.logs.MyLog;
import com.vincent.julie.service.JulieService;

/**
  * 到设置——>应用程序，找到刚才安装的应用，点击“强行停止”，那么重启手机后，就收不到BOOT_COMPLETED广播了。
  * 如果该应用被有些三方安全软件强制杀掉进程后，重启手机也会收不到BOOT_COMPLETED广播。
  * 并且在华为等自带权限管理软件的手机上，如果没有应用自动启动的权限，也是不能开机就启动的
  *@author Vincent QQ1032006226
  *created at 2016/9/27 17:35
  */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent sayHelloIntent=new Intent(context,JulieService.class);
            context.startService(sayHelloIntent);
            MyLog.d("data","收到开机广播了，启动JulieService");
        }
    }
}
