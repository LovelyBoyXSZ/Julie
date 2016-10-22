package com.vincent.julie.service;

import android.app.IntentService;
import android.content.Intent;

import com.vincent.julie.logs.MyLog;

/**
 * Created by Vincent on 2016/10/21.
 */

public class MyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    //IntentService会使用单独的线程来执行此方法
    @Override
    protected void onHandleIntent(Intent intent) {
        //该方法内可以执行任何耗时操作，比如下载文件，此处只是让线程暂停20s
        MyLog.d("MyIntentService","onHandleIntent");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        MyLog.w("MyIntentService","耗时操作执行完成");
    }

    @Override
    public void onDestroy() {
        MyLog.w("MyIntentService","死球了");
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        MyLog.w("MyIntentService","耗时操作执行完成，调用自我销毁方法");
        return super.stopService(name);
    }
}
