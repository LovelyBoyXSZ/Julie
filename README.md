#Julie Project Version
</br></br>最近项目发布了新的版本，闲来无事，整理了一下平时收集的东西，算是做了个小项目，都是基础的东西，留着以后可以用，也能看看..免得再找..

###第五次提交，收录了一个加密解密类
原文地址：http://blog.csdn.net/gzejia/article/details/52755332
</br>看了下，还不错，收藏了


###第四次提交 push log:增加一个自定义View，

from:http://gold.xitu.io/post/57fb97412e958a005596cab9 </br>
看到这个自定义View还不错，加进来，哈
* 添加自定义View：AnFQNumEditText，限制输入字数

###第三次提交 commit log:增加对华为手机管家的各种跳转
效果图:我的blog： [ 跳转到华为手机管家悬浮窗管理页面 ]( http://blog.csdn.net/pkandroid/article/details/52014653 )
</br>date: 2016年10月13日20:34:18
* 跳转到华为手机管家内存清理页面
* 跳转到华为手机管家权限管理页面
* 跳转到华为手机管家通知管理页面
* 跳转到华为手机管家悬浮窗管理页面
* 跳转到华为手机管家骚扰拦截管理页面
* 跳转到华为手机管家受保护app管理页面
* 跳转到华为手机管家自启动权限管理页面
* 跳转到华为手机管家 关联启动权限管理页面（失败，系统禁止）

###第二次提交commit log: JobCastielService 定时任务

date: 2016年10月11日00:00:27
* 添加类：JobCastielService 此类的onStart和onStop方法 作用：在杀掉Service之后启动定时任务启动JulieService，后台保活 测试：在荣耀6 android6.0 EMUI4.0.1_H60-L02_6.10.1可实现开机就运行，直到关机，荣耀8不行，会认为是关联启动

###首次代码提交：主要功能如下：
date: 2016年9月28 15:22:55
* MD5加密工具类
* 第一次进入引导页
* 欢迎页面停留显示2s
* Volley网络请求封装
* 侧滑菜单，自定义SlidingMenu实现
* 顶部状态栏颜色配置，可兼容android4.4+
* 封装title，保证风格统一，所有继承BaseActivity的Activity均自带title
* 二维码扫描、文字二维码生成，带logo的二维码生成，二维码分享（调用系统分享并保存到手机）
* 开机自启动（需要权限ps:我的手机版本bug，可开机自启动，华为荣耀6，H60-L02 EMUI4.0.1_H60-L02_6.10.1）
* 适配Android6.0之后的权限机制：permissionsdispatcher插件（不管用户是否采取哪种操作，都能给用户以提示）
* 底部4个tab切换，监听Fragment的返回事件，判定首个Fragment才能退出，类似京东首页退出效果，退出方式为2s内连点两下
* butterknife(8.4.0注解，使用方式：在setContentView的布局上右键#Generate，然后点击Generate Butterknife Injections）
* JulieService会在后台循环播放一段无声MP3来保证JulieService不被系统kill，此方法在华为手机中估计是都能实现的，而且还能避免锁屏之后被系统杀死！ps:最好是能加入到华为手机管家受保护应用中去，那样效果更好啦
* 手机信息收集：手机厂商、手机型号、SDK版本、系统版本、基带版本、内核版本、内部版本、cpu信息、屏幕宽度、屏幕高度、all运行内存、可用运行内存、IMEI号码、内置SD卡可用大小、内置SD卡大小、屏幕分辨率、是否root



