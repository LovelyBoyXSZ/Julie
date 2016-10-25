package com.vincent.julie.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.ui.activity.MainActivity;
import com.vincent.julie.ui.activity.PushMessageActivity;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.util
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/19 11:26
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class NotificationUtils {

    private static NotificationManager mNotificationManager;
    private static final int  notifyId=100;

    /**
     * 发送通知提示
     * @param context 上下文
     * @param imgId 图标id
     * @param title 通知title
     * @param msg 消息
     * @param activity 点击通知需要跳转的Activity，Activity需要完整路径，包含包名
     */
    public static void sendNotification(Context context, String activity, int imgId, String title, String msg) {
        try {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            Intent resultIntent = new Intent(context, SystemUtilts.getReflectInstance(MyApplication.getInstance(),activity).getClass());
//            mBuilder.setAutoCancel(false);//设置消息点击之后取消
            resultIntent.putExtra("title",title);
            resultIntent.putExtra("msg",msg);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(imgId)
                    .setTicker("您有新的消息")
                    .setContentTitle(title)//通知的标题
                    .setContentText(msg)//显示在界面上的内容
                    .setContentIntent(pendingIntent);
            Notification mNotification = mBuilder.build();
//        mNotification.icon = R.mipmap.et_app_icon;//设置通知  消息  图标
            mNotification.iconLevel=imgId;
            //在通知栏上点击此通知后自动清除此通知
//            mNotification.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;//设置点击之后消失
//        mNotification.defaults = Notification.DEFAULT_VIBRATE; //设置显示通知时的默认的发声、震动、Light效果
            mNotification.defaults = Notification.DEFAULT_SOUND;//声音效果，不震动
            //设置发出消息的内容
            mNotification.tickerText = "您有新的消息";//通知产生的时候发出的内容
            //设置发出通知的时间
            mNotification.when = System.currentTimeMillis();
//        startForeground(notifyId, mNotification);//把该service创建为前台service
            mNotificationManager.notify(notifyId,mNotification);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
