package com.vincent.julie.util;

import android.util.Log;

import com.vincent.julie.app.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vincent on 2016/9/27.
 */

public class ExitUtils {
    private static boolean isQuit=false;
    /**
      *true 退出
      *@author Vincent QQ1032006226
      *created at 2016/9/27 11:03
      */
    public static boolean isQuit(){
        if (isQuit == false) {
            isQuit = true;
            ToastUtils.showSingleTextToast(MyApplication.getInstance(), "再按一次离开商城");
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
            Log.d("Conversatio退出", "Conversatio退出");
            return false;
        }
    }

}
