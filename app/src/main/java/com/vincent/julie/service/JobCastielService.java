package com.vincent.julie.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.AppUtil;


/**
 * 项目名称：Julie
 * 类描述：app进程防杀，杀死之后能自动起来，确实可以做一些操作，H60-L02 安卓6.0亲测可用..
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/10 17:46
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobCastielService extends JobService {
    private int kJobId = 0;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.i("castiel", "jobService启动");
        scheduleJob(getJobInfo());
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        MyLog.i("castiel", "执行了onStartJob方法");
        boolean isLocalServiceWork = AppUtil.isServiceRunning(JobCastielService.this, "com.vincent.julie.service.JulieService");
       if(isLocalServiceWork){
           MyLog.d(JobCastielService.class.getSimpleName(),"JulieService is running");
       }else {
           startService(new Intent(JobCastielService.this,JulieService.class));
       }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        MyLog.i("castiel", "执行了onStopJob方法");
        scheduleJob(getJobInfo());
        return true;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t) {
        MyLog.i("castiel", "调度job");
        JobScheduler tm =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

    public JobInfo getJobInfo(){
        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, new ComponentName(this, JobCastielService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        //间隔100毫秒
        builder.setPeriodic(100);
        return builder.build();
    }
}
