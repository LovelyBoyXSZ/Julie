#Julie Project Version
</br></br>最近项目发布了新的版本，闲来无事，整理了一下平时收集的东西，算是做了个小项目，都是基础的东西，留着以后可以用，也能看看..免得再找..

###第六次提交：增加对Retrolambda的支持，增加对RxJava的支持,增加Retrofit网络框架支持
date:2016年10月17日12:54:03</br>
Retrolambda介绍：http://blog.csdn.net/cai_iac/article/details/50846139 
</br>Demo:https://github.com/evant/gradle-retrolambda
</br>RxJava介绍：http://gank.io/post/560e15be2dca930e00da1083#toc_1
####Retrolambda使用集成：</br>
  1、在外部build.gradle中的dependencies{}配置：
```java
   dependencies {
        classpath 'com.android.tools.build:gradle:2.2.1'
        classpath 'me.tatarka:gradle-retrolambda:3.3.0'//retrolambda 支持java1.8语法
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8' 
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```
  
  2、在app的build.gradle的顶部加入：
```java
  apply plugin: 'com.android.application'
  apply plugin: 'com.neenbedankt.android-apt'
  apply plugin: 'me.tatarka.retrolambda' //retrolambda 插件声明 设置说明：http://www.open-open.com/lib/view/open1433898197176.html
```
</br>在app的build.gradle的android{}中配置： 
```java  
  retrolambda {
        println("JAVA_HOME: " + System.getenv("JAVA_HOME"))
        println("JAVA7_HOME: " + System.getenv("JAVA7_HOME"))
        println("JAVA8_HOME: " + System.getenv("JAVA8_HOME"))
        javaVersion JavaVersion.VERSION_1_7
    }
    /**
     * 加入compileOptions,这会让IDE使用用JAVA8语法解析
     */
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    /**
     * 指定将源码编译的级别,，使用下列代码，会将代码编译到兼容1.6的字节码格式
     */
    retrolambda {
        javaVersion JavaVersion.VERSION_1_6
    }
```
  
  </br>设置说明：http://www.open-open.com/lib/view/open1433898197176.html
  </br>3、在app的build.gradle中的dependencies{}引入：
  
```java
  dependencies{
   //Retrolambda的使用 设置说明：http://www.open-open.com/lib/view/open1433898197176.html
    compile 'me.tatarka:gradle-retrolambda:3.3.0'
   //Android开发之RxJava和RxAndroid RxJava说明:http://gank.io/post/560e15be2dca930e00da1083#toc_1
    compile 'io.reactivex:rxjava:1.2.1'
    compile 'io.reactivex:rxandroid:1.2.1'
    //retrofit网络访问框架
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
  //compile 'com.squareup.retrofit:adapter-rxjava:2.0.0-beta2'
  }
```
 
####Retrolambda使用说明：
```java
        //使用Retrolambda插件之前的写法
       tvRetrolambdaTest.setOnClickListener(new View.OnClickListener() {//使用Retrolambda插件之前的写法
            @Override
            public void onClick(View v) {
                ToastUtils.showSingleTextToast(RxJavaTestActivity.this,"Retrolambda学习，弹出来一个Toast");
            }
        });
        //使用Retrolambda插件之后的写法
        tvRetrolambdaTest.setOnClickListener(view -> ToastUtils.showSingleTextToast(this, "Retrolambda学习，弹出来一个Toast"));
```
####RxJava使用
* RxJava的使用配置：
</br>配置在上面...
* RxJava的使用：
</br>.....
</br>####Retrofit网络框架请求聚合天气数据
* 建立天气实体类WeatherEntity.class
```java
 package com.vincent.julie.retrofit.model;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.entity
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/17 16:43
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class WeatherEntity {

    private String resultcode;//响应状态码  200-->获取数据成功
    private String reason;// successed  响应状态
    private String error_code;//错误码
    private result result;

    public WeatherEntity.result getResult() {
        return result;
    }

    public void setResult(WeatherEntity.result result) {
        this.result = result;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public static class result{
        private today today;
        private sk sk;

        public WeatherEntity.result.today getToday() {
            return today;
        }

        public void setToday(WeatherEntity.result.today today) {
            this.today = today;
        }

        public WeatherEntity.result.sk getSk() {
            return sk;
        }

        public void setSk(WeatherEntity.result.sk sk) {
            this.sk = sk;
        }

        public class sk{
            private String temp;//28
            private String wind_direction;//东北风
            private String wind_strength;//1级
            private String humidity;//"69%
            public String time;//时间
        }
        public class today{
            private String wind;//威风
            private String week;//星期一

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }
        }
    }

}
```
* 创建请求接口WeatherApiService.class
```java
package com.vincent.julie.retrofit;

import com.vincent.julie.retrofit.model.WeatherEntity;
import com.vincent.julie.retrofit.response.GetWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.listener
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/17 16:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public interface WeatherApiService {
//天气地址：http://v.juhe.cn/weather/index?format=2&cityname=深圳&key=77a262c554de40916edc78858221b4a9
/*
    @GET("weather/index")
    Observable<GetWeatherData> getWeatherData(@Query("format") String format, @Query("cityname" )String cityname, @Query("key")String appKey);
*/
    @GET("weather/index")
    Call<WeatherEntity> getWeatherData(@Query("format")String format, @Query("cityname")String cityname, @Query("key")String appKey);

}

```
* 在RetrofitActivity.class中发起请求

```java
Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService weatherApiService=retrofit.create(WeatherApiService.class);
        final Call<WeatherEntity> call=weatherApiService.getWeatherData("2","深圳","77a262c554de40916edc78858221b4a9");
        new Thread(){
            @Override
            public void run() {
                try{
                    Response<WeatherEntity> bodayResponse=call.execute();
                    weatherEntity=bodayResponse.body();
                    MyLog.d("data",weatherEntity.getReason()+","+weatherEntity.getError_code()+","+weatherEntity.getResult().toString());
                    ToastUtils.showSingleTextToast(MyApplication.getInstance(),weatherEntity.getReason());
                    WeatherEntity.result.today today=weatherEntity.getResult().getToday();
                    MyLog.d("today",today.toString());
                    Message message=Message.obtain();
                    message.obj=weatherEntity;
                    message.arg1=0;
                    handler.sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }catch (Exception e){
                    MyLog.d("data",weatherEntity.getReason()+","+weatherEntity.getError_code());
                    e.printStackTrace();
                }
            }
        }.start();
```
在handle的handleMessage（Message message）方法中处理tvShowWeather显示数据更新，这里只处理了星期
```java
if(msg.arg1==0){
                WeatherEntity weatherEntity=(WeatherEntity) msg.obj;
                tvShowWeather.setText(weatherEntity.getResult().getToday().getWeek());
            }
```


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



