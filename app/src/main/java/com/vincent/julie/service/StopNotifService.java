package com.vincent.julie.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.vincent.julie.R;
import com.vincent.julie.logs.MyLog;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.service
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/19 15:26
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class StopNotifService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//启动一个线程，停止通知，此时通知变的不可见，但是后台Service还在
        MyLog.d("StopNotifService","onStartCommand()调用");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(250, builder.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                stopForeground(true);
                NotificationManager manager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(250);
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
