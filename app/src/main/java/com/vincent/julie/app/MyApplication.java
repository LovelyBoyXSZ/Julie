package com.vincent.julie.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ExampleUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Vincent on 2016/9/26.
 */


public class MyApplication extends Application {

    private static MyApplication app;
    /**
     * 管理Activity
     *
     * @author Vincent
     * create at 2016/9/27 22:23
     */
    private static List<Activity> actList;

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);//使用OkGo必须在MyApplication中初始化
        Logger.init("Julie");
        if(Contants.LOG_SWITCH){//跟随app日志开关
            JPushInterface.setDebugMode(true);//测试模式，打开调试日志
        }else {
            JPushInterface.setDebugMode(false);
        }
        JPushInterface.init(getApplicationContext());//JPush初始化
        app = this;
//        Fresco.initialize(this);//图片加载库初始化 使用SimpleDraweeView控件加载图片
        actList = new ArrayList<>();
        MyLog.d("设备Id", ExampleUtil.getDeviceId(this));
        MyLog.d("RegistrationID",JPushInterface.getRegistrationID(this));
        System.out.print(JPushInterface.getRegistrationID(this));
        MyLog.d("UDID",JPushInterface.getUdid(this));//设备Id==Uid
        MyLog.d("AppKey",ExampleUtil.getAppKey(this));
        MyLog.d("IMEI",ExampleUtil.getImei(this,"IMEI获取失败"));
    }

    /**
     * 目的：解决方法数65535限制
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 返回一个Application对象
     *
     * @return
     */
    public static MyApplication getInstance() {
        return app;
    }

    /**
     * 非继承BaseActivity的Activity应该手动在onCreate方法中添加此方法
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 17:15
     */
    public static void addActivity(Activity activity) {
        actList.add(activity);
    }

    /**
     * 非继承BaseActivity的Activity应该手动在onDestory方法中添加此方法
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 17:17
     */
    public static void removeActiivty(Activity activity) {
        actList.remove(activity);
    }

    /*
     * a
     *移除所有的Activity
     * @author Vincent QQ:1032006226
     * create at 2016/9/27 22:28
     */
    public static void removeAllActivity() {
        for (Activity activity : actList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        if (actList.size() > 0) {
            MyLog.w("removeAllActivity方法调用", "actList is not null");
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 11:54
     */
    public static int getScreenParameterWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) getInstance().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕宽度（像素）
        int w = width + height;
        return width;
    }

    /**
     * 屏幕高度
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 11:54
     */
    public static int getScreenParameterHeight() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) getInstance().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕宽度（像素）
        int w = width + height;
        return height;
    }
}
