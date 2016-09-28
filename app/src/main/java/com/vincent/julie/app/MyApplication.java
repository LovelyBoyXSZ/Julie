package com.vincent.julie.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vincent.julie.logs.MyLog;

import java.util.ArrayList;
import java.util.List;

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
        app = this;
        Fresco.initialize(this);//图片加载库初始化 使用SimpleDraweeView控件加载图片
        actList = new ArrayList<>();
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
