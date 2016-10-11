package com.vincent.julie.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vincent.julie.R;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.AppUtil;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.service
 * 类描述：检查JulieService时候运行
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/11 10:08
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */
public class ProtectService extends Service{

    private static final String TAG=ProtectService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 此广播监听系统时间变化、系统每分钟发一次ACTION_TIME_TICK
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(bro, filter);
        MyLog.d(TAG,"创建ProtectService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bro);
    }

    private final BroadcastReceiver bro=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
                MyLog.d(TAG,"-_-");
                checkService();
            }
        }
    };

    /**
     * 〈检查JulieService时候正在运行〉
     * 〈功能详细描述〉
     * 创建时间：2016/10/11 10:11
     * @author Vincent
     */
    private void checkService(){
        if(!AppUtil.isServiceRunning(ProtectService.this,"com.vincent.julie.service.JulieService")){
            MyLog.d(TAG,"JulieService is stop,start JulieService ...");
            startService(new Intent(ProtectService.this,JulieService.class));
        }else {
            MyLog.d(TAG,"JulieService is running");
        }
    }
}
