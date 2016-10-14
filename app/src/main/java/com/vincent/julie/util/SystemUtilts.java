package com.vincent.julie.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.Toast;

import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 设置手机顶部状态栏颜色
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 9:13
 */
public class SystemUtilts {
    private static final String TAG = SystemUtilts.class.getSimpleName();

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneMdel() {
        String s = Build.MODEL;
        if (TextUtils.isEmpty(s)) {
            return "未知";
        }
        return s;

    }

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getPhoneManufacturer() {
        String phoneManufacturer = Build.MANUFACTURER;
        return phoneManufacturer;
    }

    public static String getVersionOs() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 返回系统版本号
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK);
            if (version == 0) {
                return 0;
            }
        } catch (NumberFormatException e) {
            MyLog.e(e.toString(), null);

        }
        return version;
    }

    /**
     * 获取CPU型号
     *
     * @return
     */
    public static String getCpuName() {

        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;

    }

    /**
     * 获取手机的IMEI号码
     *
     * @param context
     * @return
     */
    public static String getIMEINumber(Context context) {
        try {
            String imei = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            MyLog.d(TAG, imei);
//        不过纯APP开发SystemProperties，TelephonyProperties汇报错误，因为android.os.SystemProperties在SDK的库中是没有的，
//        需要把Android SDK 目录下data下的layoutlib.jar文件加到当前工程的附加库路径中，就可以Import。
//        如果Android Pad没有IMEI,用此方法获取设备ANDROID_ID：
//        String IMEI =SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI)
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    /**
     * 获取手机号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */
    public static String getBaseband_Ver() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
// System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 内核版本
     * return String
     */
    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
// get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
        String result = "";
        String line;
// get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 内部版本
     * return String
     */
    public static String getInner_Ver() {
        String ver = "";
        if (Build.DISPLAY.contains(Build.VERSION.INCREMENTAL)) {
            ver = Build.DISPLAY;
        } else {
            ver = Build.VERSION.INCREMENTAL;
        }
        return ver;
    }

    /**
     * 获取手机的可用内存大小
     *
     * @param context
     * @return
     */
    public static String getPhoneRAM(Context context) {
//        要获取手机的可用内存，首先要获取系统服务信息，
        ActivityManager myActivityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
//        然后获得MemoryInfo类型对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        然后，使用getMemoryInfo(memoryInfo)方法获得系统可用内存，此方法将内存大小保存在memoryInfo对象上
        myActivityManager.getMemoryInfo(memoryInfo);
//        然后，memoryInfo对象上的availmem值即为所求
        long memSize = memoryInfo.availMem;
//        字符类型转换 ，转换成MB格式。
        String leftMemSize = Formatter.formatFileSize(context, memSize);

        return leftMemSize;
    }

    /**
     * 获取手机总的运行内存
     *
     * @param context
     * @return
     */
    public static String getTotalMemory(Context context) {
        long size = 0;
        Double d = 0.0;
        //通过读取配置文件方式获取总内大小。文件目录：/proc/meminfo
        File file = new File("/proc/meminfo");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //根据命令行可以知道，系统总内存大小位于第一行
            String totalMemarysizeStr = reader.readLine();//MemTotal:         513744 kB
            //要获取大小，对字符串截取
            int startIndex = totalMemarysizeStr.indexOf(':');
            int endIndex = totalMemarysizeStr.indexOf('k');
            //截取
            totalMemarysizeStr = totalMemarysizeStr.substring(startIndex + 1, endIndex).trim();
            //转为long类型，得到数据单位是kb
            size = Long.parseLong(totalMemarysizeStr);
            //转为以byte为单位
            size *= 1024;
            d = (Double) (size / 1024.0 / 1024.0 / 1024.0);//转换单位为G
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保留两位小数
        return String.valueOf(new DecimalFormat("#.00").format(d));
    }


    /**
     * 判断手机是否Root
     *
     * @return
     * @throws Exception
     */
    public static boolean isRoot() throws Exception {
        boolean isRoot = false;
        File su = new File("/system/bin/su");
        File su2 = new File("/system/bin/su");
        if (su.exists() && su2.exists()) {
            isRoot = true;
        } else {
            isRoot = false;
        }
        return isRoot;
    }

    /**
     * 获取外置SD卡路径以及TF卡的路径
     * <p>
     * 返回的数据：paths.get(0)肯定是外置SD卡的位置，因为它是primary external storage.
     *
     * @return 所有可用于存储的不同的卡的位置，用一个List来保存
     */
    public static List<String> getExtSDCardPathList() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        //首先判断一下外置SD卡的状态，处于挂载状态才能获取的到
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            //外置SD卡的路径
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                //扩展存储卡即TF卡或者SD卡路径
                paths.add(mountPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" /> 需要权限
     *
     * @param context
     * @return
     */
    public static String getIMIENumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 判断应用是在前台还是后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    MyLog.e("后台", appProcess.processName);
                    return true;
                } else {
                    MyLog.e("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 得到系统可用内存
     **/
    @SuppressLint("NewApi")
    public static String getMemFree(Context context) {
        StatFs fs = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(context, (fs.getAvailableBytes()));
    }

    /**
     * 内置SD卡总容量
     *
     * @param context
     * @return
     */
    public static String getSDSize(Context context) {
        File path = Environment.getExternalStorageDirectory();//得到SD卡的路径
        StatFs stat = new StatFs(path.getPath());//创建StatFs对象，用来获取文件系统的状态
        long blockCount = stat.getBlockCount();
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        String totalSize = Formatter.formatFileSize(context, blockCount * blockSize);//格式化获得SD卡总容量
        String availableSize = Formatter.formatFileSize(context, blockCount * availableBlocks);//获得SD卡可用容量
//        tv.setText("SD卡总容量:"+totalSize+"\nSD卡可用容量:"+availableSize+"\n"+getRomSpace());
        return totalSize;
    }

    /**
     * 获得内置SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        MyLog.d("size", Formatter.formatFileSize(context, blockSize * totalBlocks));
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }


    /**
     * 判断某个服务是否正在运行的Method
     *
     * @param mContext
     * @param serviceName 是包�?+服务的类名（例如：net.loonggg.testbackstage.TestService�?
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 开启权限
     *
     * @param context
     */
    public static void goSetting(Context context) {
        try {
            Intent intent = new Intent("com.shangyi.sayimo");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            //抛出异常时提示信息
            Toast.makeText(context, "进入失败手动进入", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 判断某个APP是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 跳转到华为手机管家悬浮窗权限管理页面
     */
    public static void goHuaWeiSetting() {
        try {
            //HUAWEI H60-l02 P8max测试通过
            MyLog.d(".......", "进入指定app悬浮窗管理页面失败，自动进入所有app悬浮窗管理页面");
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//   ComponentName comp = new ComponentName("com.huawei.systemmanager",
//      "com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        } catch (SecurityException e) {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
//      ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
            MyLog.d("....", "正在进入指定app悬浮窗开启位置..");
        } catch (ActivityNotFoundException e) {
            /**
             * 手机管家版本较低 HUAWEI SC-UL10
             */
//   Toast.makeText(MainActivity.this, "act找不到", Toast.LENGTH_LONG).show();
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");//权限管理页面 android4.4
//   ComponentName comp = new ComponentName("com.android.settings","com.android.settings.permission.single_app_activity");//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
            e.printStackTrace();
        } catch (Exception e) {
            //抛出异常时提示信息
            Toast.makeText(MyApplication.getInstance(), "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 权限管理主页
     *  com.huawei.systemmanager/com.huawei.permissionmanager.ui.MainActivity
     */
    public static void goHWWindowPermission(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }

    /**
     * 华为手机管家自启动管理
     * com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity
     */
    public static void goHWSelfMotionStartManager(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
    /**
     * 关联启动
     * com.huawei.systemmanager/.startupmgr.ui.StartupAwakedAppListActivity
     *  java.lang.SecurityException: Permission Denial: starting Intent { act=com.vincent.julie flg=0x10000000 cmp=com.huawei.systemmanager/.startupmgr.ui.StartupAwakedAppListActivity } from ProcessRecord{bdb00e8 31867:com.vincent.julie/u0a156} (pid=31867, uid=10156) not exported from uid 1000
     */
    public static void goRelevanceStartManager(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupAwakedAppListActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (SecurityException e){
            ToastUtils.showSingleTextToast(MyApplication.getInstance(),"安全异常，未获取许可，打开失败");
            e.printStackTrace();
        }catch (Exception e){
            ToastUtils.showSingleTextToast(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
    /**
     * 受保护的app 锁屏可以继续运行
     * com.huawei.systemmanager/.optimize.process.ProtectActivity
     */
    public static void goProtectAppManager(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
    /**
     * 华为手机管家通知管理页面
     * com.huawei.systemmanager/com.huawei.notificationmanager.ui.NotificationManagmentActivity
     */
    public static void goNotificationManager(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
    /**
     * 华为手机管家清理加速页面
     * com.huawei.systemmanager/.spacecleanner.SpaceCleanActivity
     */
    public static void goClearMemory(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.spacecleanner.SpaceCleanActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
    /**
     * 华为手机管家骚扰拦截页面
     * com.huawei.systemmanager/com.huawei.harassmentinterception.ui.InterceptionActivity
     */
    public static void goInterceptionAct(){
        try {
            Intent intent = new Intent("com.vincent.julie");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.harassmentinterception.ui.InterceptionActivity");
            intent.setComponent(comp);
            MyApplication.getInstance().startActivity(intent);
        }catch (Exception e){
            ToastUtils.showSingleToastCenter(MyApplication.getInstance(),"打开失败");
            e.printStackTrace();
        }
    }
}
