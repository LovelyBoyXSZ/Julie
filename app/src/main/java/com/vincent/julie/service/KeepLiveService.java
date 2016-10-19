package com.vincent.julie.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.vincent.julie.R;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.service
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/19 15:04
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class KeepLiveService extends Service {

    private static boolean sPower=true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {//启动通知
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(250, builder.build());
        } else {
            startForeground(250, new Notification());//4.3一下空通知保活
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sPower) {
                    if (System.currentTimeMillis() >= 123456789000000L) {
                        sPower = false;
                    }
                    SystemClock.sleep(3000);
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
