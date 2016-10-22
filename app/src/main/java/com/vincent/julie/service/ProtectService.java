package com.vincent.julie.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vincent.julie.R;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.AppUtil;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

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
    private TelephonyManager tManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       MyLog.d("ProjectService","onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener listener=new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        MyLog.w(ProtectService.class.getSimpleName(),"当前无任何状态");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        MyLog.d(TAG,"CALL_STATE_OFFHOOK");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        MyLog.w(ProtectService.class.getSimpleName(),"来电啦");
                        OutputStream os=null;
                        try {
                            os=openFileOutput("phoneList",MODE_APPEND);
                            PrintStream ps=new PrintStream(os);
                            //将来电号码记录到文件中
                            ps.println(new Date()+"来电:"+incomingNumber);
                            ps.close();
                        }catch (Exception e){
                            e.printStackTrace();
                            MyLog.w(TAG,"来电号码保存失败");
                        }
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        tManager.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);


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
                checkCallPhone();
            }
        }
    };

    /**
     * 监控手机来电
     */
    private void checkCallPhone() {

    }

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
