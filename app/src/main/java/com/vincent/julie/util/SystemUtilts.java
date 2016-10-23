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
import android.text.format.Formatter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 设置手机顶部状态栏颜色
 *方法列表：
 *  获取手机的IMEI号码，
 *  获取手机号码，
 *  基带版本，
 *  内核版本，
 *  内部版本，
 *  获取手机的可用内存大小，
 *  获取手机总的运行内存
 *  判断手机是否Root
 *  判断应用是在前台还是后台，
 *  得到系统可用内存，
 *  内置SD卡总容量，
 *  获得内置SD卡总大小，
 *  判断某个服务是否正在运行的Method
 *  开启悬浮窗权限oppo手机，
 *  判断某个APP是否安装，
 *  跳转到华为手机管家悬浮窗权限管理页面，
 *  跳转到华为手机管家权限管理主页，
 *  华为手机管家自启动管理
 *  跳转到华为手机管家关联启动页面，
 *  跳转到受保护的app管理页面锁屏可以继续运行，
 *  跳转到华为手机管家通知管理页面，跳转到华为手机管家清理加速页面
 *  跳转到华为手机管家骚扰拦截页面，
 *  通过反射获取类对象，
 *  getInputKeyboard(EditText editText) 自动弹出键盘
 *  closeInputKeyBoard(EditText editText) 关闭软键盘
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
        try {
            return Build.MODEL;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getPhoneManufacturer() {
        try {
            return Build.MANUFACTURER;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 系统版本
     * @return
     */
    public static String getVersionOs() {
        try {
            return Build.VERSION.RELEASE;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 返回SDK版本号
     *
     * @return
     */
    public static String getAndroidSDKVersionStr() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK);
                return Build.VERSION.SDK;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            MyLog.e("获取系统版本号失败",e.toString());
            return "获取失败";
        }
    }

    /**
     * 返回系统版本号
     *
     * @return
     */
    public static int getAndroidSDKVersionInt() {
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
            e.printStackTrace();
            return "cpu信息获取失败";
        }
        return "获取失败";
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
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(TELEPHONY_SERVICE);
            return telephonyManager.getLine1Number();
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
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
            e.printStackTrace();
            return "获取失败";
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
            return "获取失败";
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 内部版本
     * return String
     */
    public static String getInner_Ver() {
        try {
            String ver = "";
            if (Build.DISPLAY.contains(Build.VERSION.INCREMENTAL)) {
                ver = Build.DISPLAY;
            } else {
                ver = Build.VERSION.INCREMENTAL;
            }
            return ver;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 获取手机的可用内存大小
     *
     * @param context
     * @return
     */
    public static String getPhoneRAM(Context context) {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
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
            return String.valueOf(new DecimalFormat("#.00").format(d));//保留两位小数
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }


    /**
     * 判断手机是否Root
     *
     * @return
     * @throws Exception
     */
    public static boolean isRoot(){
        try {
            boolean isRoot = false;
            File su = new File("/system/bin/su");
            File su2 = new File("/system/bin/su");
            if (su.exists() && su2.exists()) {
                isRoot = true;
            } else {
                isRoot = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
        try {
            StatFs fs = new StatFs(Environment.getDataDirectory().getPath());
            return Formatter.formatFileSize(context, (fs.getAvailableBytes()));
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 内置SD卡总容量
     *
     * @param context
     * @return
     */
    public static String getSDSize(Context context) {
        try {
            File path = Environment.getExternalStorageDirectory();//得到SD卡的路径
            StatFs stat = new StatFs(path.getPath());//创建StatFs对象，用来获取文件系统的状态
            long blockCount = stat.getBlockCount();
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            String totalSize = Formatter.formatFileSize(context, blockCount * blockSize);//格式化获得SD卡总容量
            String availableSize = Formatter.formatFileSize(context, blockCount * availableBlocks);//获得SD卡可用容量
//        tv.setText("SD卡总容量:"+totalSize+"\nSD卡可用容量:"+availableSize+"\n"+getRomSpace());
            return totalSize;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 获得内置SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            MyLog.d("size", Formatter.formatFileSize(context, blockSize * totalBlocks));
            return Formatter.formatFileSize(context, blockSize * totalBlocks);
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }


    /**
     * 判断某个服务是否正在运行的Method
     *
     * @param mContext
     * @param serviceName 是包�?+服务的类名（例如：net.loonggg.testbackstage.TestService�?
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 开启悬浮窗权限,oppo手机
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
     * 跳转到华为手机管家权限管理主页
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
     *跳转到华为手机管家关联启动页面
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
    //HwSystemManager: HsmStat_event:"37","{PKG:com.shangyi.netty,OP:1}"
    //com.huawei.systemmanager
//    com.android.settings

    /**
     * 通过反射获取类对象
     * @param context
     * @param className 包含包名
     * @return
     */
    public static Object getReflectInstance(Context context, String className){
        try {
            //获取Student的Class对象
            Class<?> clazz = Class.forName(className);
            //获取该类中所有的属性
            Field[] fields = clazz.getDeclaredFields();
            //遍历所有的属性
            for (Field field : fields) {
                //打印属性信息，包括访问控制修饰符，类型及属性名
                System.out.println(field);
                System.out.println("修饰符：" + Modifier.toString(field.getModifiers()));
                System.out.println("类型：" + field.getType());
                System.out.println("属性名：" + field.getName());
            }
            //获取该类中的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //打印方法签名
                System.out.println(method);
                System.out.println("修饰符：" + Modifier.toString(method.getModifiers()));
                System.out.println("方法名：" + method.getName());
                System.out.println("返回类型：" + method.getReturnType());
                //获取方法的参数对象
                Class<?>[] clazzes = method.getParameterTypes();
                for (Class<?> class1 : clazzes) {
                    System.out.println("参数类型：" + class1);
                }
            }
            //通过Class对象创建实例
//            Student student = (Student)clazz.newInstance();
//            //获取属性名为studentName的字段(Field)对象，以便下边重新设置它的值
//            Field studentName = clazz.getField("studentName");
//            //设置studentName的值为”张三“
//            studentName.set(student, "张三");
//
//            //通过Class对象获取名为”finishTask“，参数类型为String的方法(Method)对象
//            Method finishTask = clazz.getMethod("finishTask", String.class);
//            //调用finishTask方法
//            finishTask.invoke(student, "数学");
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自动弹出键盘
     * @param editText
     */
    public static void getInputKeyboard(EditText editText){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        MyLog.d(SystemUtilts.class.getSimpleName(),"尝试弹出软键盘");
                        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(editText.getWindowToken(),InputMethodManager.SHOW_IMPLICIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 关闭键盘
     */
    public static void closeInputKeyBoard(EditText editText){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken() , 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
