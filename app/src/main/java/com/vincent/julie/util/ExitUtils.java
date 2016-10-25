package com.vincent.julie.util;

import android.content.Context;
import android.util.Log;

import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vincent on 2016/9/27.
 */

public class ExitUtils {
    private static boolean isQuit=false;
    /**
     * 在onBackPressed()方法中调用
      *true 退出
      *@author Vincent QQ1032006226
      *created at 2016/9/27 11:03
      */
    public static boolean isQuit(Context context,String exitMsg){
        if (isQuit == false) {
            isQuit = true;
            ToastUtils.showSingleTextToast(context.getApplicationContext(), exitMsg);
            TimerTask task = null;
            task = new TimerTask() {
                @Override
                public void run() {
                    isQuit = false;
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 2000);
            return true;
        } else {
            MyApplication.removeAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            MyLog.d("app is exit", "app is exit");
            return false;
        }
    }

}
