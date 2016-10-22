package com.vincent.julie.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;


import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppUtil {
	/**
	 * 启动另外一个app里面的activity
	 * @param packageName 指的是你要启动的app的主要的包名 配置文件里面顶部的packname
	 * @param activityName 你要启动的activity的包含完整包名的Activity
	 *  注意：   需要启动的Activity需要配置<intent-filter> <action android:name="android.intent.action.View/> </intent-filter>
     */
	public static void startOtherAppActivity(String packageName,String activityName){
		try {
			Intent intent=new Intent();
			ComponentName componentName=new ComponentName(packageName,activityName);
			intent.setComponent(componentName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果没有设置，则会报错 AndroidRuntimeException
			MyApplication.getInstance().startActivity(intent);
			MyLog.w("AppUtils->startOtherAppActivity","正在启动"+activityName+",请稍后..");
			ToastUtils.showDefaultToast(MyApplication.getInstance(),"正在启动..");
		}catch (Exception e){
			e.printStackTrace();
			MyLog.w("AppUtils->startOtherAppActivity","启动失败了，我猜测是没有权限吧");
			ToastUtils.showDefaultToast(MyApplication.getInstance(),"启动失败");
		}
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param mContext
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(9999);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 获取所有启动的app包名
	 * 
	 * @param context
	 * @return
	 */
	public static Set<String> getAllApp(Context context) {
		Set<String> result = new HashSet<String>();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent("android.intent.action.MAIN", null);
		intent.addCategory("android.intent.category.LAUNCHER");
		List<ResolveInfo> list = packageManager
				.queryIntentActivities(intent, 0);
		if (null != list) {
			for (ResolveInfo info : list) {
				result.add(info.activityInfo.packageName);
			}
		}

		return result;
	}

	/**
	 * 检测app是否已经安装
	 * 
	 * @param context
	 * @param packeName
	 *            完整包名
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packeName) {
		if (null == packeName || "".equals(packeName)) {
			return false;
		}
		Set<String> allPackgroundNames = getAllApp(context);
		if (null != allPackgroundNames
				&& allPackgroundNames.contains(packeName)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断应用是在前台还是后台
	 */

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.e("后台", appProcess.processName);
					return true;
				} else {
					Log.e("前台", appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}


	/**
	 *  判断当前应用程序处于前台还是后台
	 *
	 *  需要权限： android.permission.GET_TASKS
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				Log.d("tag", " application is background ");
				return true;
			}
		}

		Log.d("tag", " application is foreground ");
		return false;
	}

	/**
	 * 检测当前运行的应用第一个启动的activity是否指定的activity
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean isFirstActivity(Context context, String name){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName baseActivity = tasks.get(0).baseActivity;
			if (baseActivity.getClassName().equals(name)) {
				Log.d("tag", " baseActivity is " + name);
				return true;
			}
			Log.d("tag", " baseActivity is " + baseActivity.getClassName());
		}

		Log.d("tag", " baseActivity is empty ");
		return false;
	}

	/**
	 * 检测当前运行的应用正在运行的activity是否指定的activity
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean isTopActivity(Context context, String name){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName baseActivity = tasks.get(0).topActivity;
			if (baseActivity.getClassName().equals(name)) {
				Log.d("tag", " topActivity is " + name);
				return true;
			}
			Log.d("tag", " topActivity is " + baseActivity.getClassName());
		}

		Log.d("tag", " topActivity is empty ");
		return false;
	}

	/**
	 * 重启APP
	 * @param context
	 * @param appPackname
     */
	public static void restartApp(Context context,String appPackname){
		MyApplication.removeAllActivity();
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackname);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		MyApplication.getInstance().startActivity(intent);
	}

	//版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	//版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}
	
}
