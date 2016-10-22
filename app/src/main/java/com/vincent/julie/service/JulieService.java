package com.vincent.julie.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.vincent.julie.R;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.ui.activity.MainActivity;
import com.vincent.julie.util.AppUtil;
import com.vincent.julie.util.ScreenOpenCloseListener;
import com.vincent.julie.util.SharedPreferencesUtil;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vincent on 2016/7/15.
 * about service please click url= http://blog.csdn.net/javazejian/article/details/52709857
 */
public class JulieService extends Service {
    private static final String TAG = JulieService.class.getSimpleName();
    private BroadcastReceiver mBR;
    private IntentFilter mIF;
    int notifyId = 101;
    private ScreenOpenCloseListener screenOpenCloseListener;
    private NotificationManager mNotificationManager;//Notification管理

    private MediaPlayer mpMediaPlayer;


    @Override
    public IBinder onBind(Intent intent) {
        MyLog.d(TAG, "onBind");
        return null;
    }

    @SuppressWarnings("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        try {
            if (mpMediaPlayer == null) {
                mpMediaPlayer = MediaPlayer.create(this, R.raw.dd);
            }
            play();//后台播放空音乐，此方法可以在系统直接清理的时候不清理本app，但是用户可以上滑结束app
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.d("Julie", "后台播放音乐出问题了");
        }
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(JulieService.this,ProtectService.class));
//        startService(new Intent(JulieService.this,KeepLiveService.class));
        MyLog.d(TAG, "onCreate");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sendNotification();
        try {
            play();//后台播放空音乐，此方法可以在系统直接清理的时候不清理本app，但是用户可以上滑结束app
        } catch (Exception e) {
            e.printStackTrace();
        }

        screenOpenCloseListener = new ScreenOpenCloseListener(this);
        screenOpenCloseListener.begin(new ScreenOpenCloseListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {//开锁
//                ToastUtil.defaultToast(SupplierService.this, "用户打开了屏幕");
                MyLog.d(TAG, "用户打开了屏幕");
            }

            @Override
            public void onScreenOff() {//锁屏
//                ToastUtil.defaultToast(SupplierService.this, "用户锁屏了");
                MyLog.d(TAG, "用户锁屏了");
            }

            @Override
            public void onUserPresent() {//解锁
//                ToastUtil.defaultToast(SupplierService.this, "用户解锁了屏幕");
                MyLog.d(TAG, "用户解锁了屏幕");
            }
        });

        mBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent a = new Intent(JulieService.this, JulieService.class);
                startService(a);
            }
        };
        mIF = new IntentFilter();
        mIF.addAction("listener");
        registerReceiver(mBR, mIF);
        /**
         * 此广播监听系统时间变化、系统每分钟发一次ACTION_TIME_TICK
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);

    }

    protected void play() {
        MyLog.d(TAG, "play");
        try {
//            mpMediaPlayer.prepare();
            if (mpMediaPlayer == null) {
                mpMediaPlayer = MediaPlayer.create(this, R.raw.dd);
            }
            //防止IllegalStateException异常
            if (mpMediaPlayer.isPlaying()) {
                mpMediaPlayer.stop();
                mpMediaPlayer.release();
                mpMediaPlayer = MediaPlayer.create(this, R.raw.dd);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mpMediaPlayer.start();
                }
            }).start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mpMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//设置重复播放
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               /* mpMediaPlayer.start();
                mpMediaPlayer.setLooping(true);*/
                play();//结束之后继续
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.d(TAG, "SupplierService--onDestroy()调用，SupplierService关闭");
        Intent intent = new Intent();//服务被杀死发送广播启动SecondService
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("START_SECONDSERVICE");
        sendBroadcast(intent);
        MyLog.d(TAG, "SupplierService-->onDestory()调用");
        unregisterReceiver(mBR);
        unregisterReceiver(receiver);
        mpMediaPlayer = null;
    }

    //释放资源
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 当程序在后台运行时执行下面的方法
     */
    private void sendNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("通知")
                .setContentTitle("666")//通知的标题
                .setContentText("来呀，互相伤害啊，弄死我啊")//显示在界面上的内容
                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
//        mNotification.icon = R.mipmap.et_app_icon;//设置通知  消息  图标
        mNotification.iconLevel=R.mipmap.ic_launcher;
        //在通知栏上点击此通知后自动清除此通知
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
//        mNotification.defaults = Notification.DEFAULT_VIBRATE; //设置显示通知时的默认的发声、震动、Light效果
        mNotification.defaults = Notification.DEFAULT_SOUND;//声音效果，不震动
        //设置发出消息的内容
        mNotification.tickerText = "朕来了";//通知产生的时候发出的内容
        //设置发出通知的时间
        mNotification.when = System.currentTimeMillis();
        startForeground(notifyId, mNotification);//把该service创建为前台service
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                Date curDate = new Date(System.currentTimeMillis());
                MyLog.d(TAG, "收到广播ACTION_TIME_TICK");
                ToastUtils.showSingleTextToast(getApplicationContext(), curDate.toString());
                if (null == mpMediaPlayer) {
                    mpMediaPlayer = MediaPlayer.create(context, R.raw.dd);
                }
                if (mpMediaPlayer.isPlaying()) {
                    mpMediaPlayer.stop();
                    mpMediaPlayer.release();
                    mpMediaPlayer = MediaPlayer.create(context, R.raw.dd);
                }
                mpMediaPlayer.start();
                //判断ProtectService时候正在启动，若没有running就start
                if(!AppUtil.isServiceRunning(JulieService.this,"com.vincent.julie.service.ProtectService")){
                    MyLog.d(TAG,"ProtectService is stop,start ProtectService ...");
                    startService(new Intent(JulieService.this,ProtectService.class));
                }else {
                    MyLog.d(TAG,"ProtectService is running");
                }
            }
        }
    };

}
