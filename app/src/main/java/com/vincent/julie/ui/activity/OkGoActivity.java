package com.vincent.julie.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.okgo.WeatherCallBack;
import com.vincent.julie.retrofit.model.WeatherEntity;
import com.vincent.julie.util.ToastUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：OkGo网络请求框架学习
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/18 11:30
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class OkGoActivity extends BaseActivity {

    private static final String SERVICE_URI = "http://v.juhe.cn/weather/index?format=2&cityname=深圳&key=77a262c554de40916edc78858221b4a9";
    @BindView(R.id.tv_get_weather_data_1)
    TextView tvGetWeatherData1;
    @BindView(R.id.tv_get_weather_data_2)
    TextView tvGetWeatherData2;

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_okgo);
        setTitleText("OkGo网络框架使用学习");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_get_weather_data_1, R.id.tv_get_weather_data_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_weather_data_1:
                try {
                   /* OkGo.get(SERVICE_URI)
                            .tag(this)
                            .cacheKey("cachekey")
                            .cacheMode(CacheMode.DEFAULT)
                            .execute(new StringCallback(){

                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if(s!=null){
                                        MyLog.d("data",s);

                                    }
                                }
                            });*/
                    OkGo.get(SERVICE_URI)
                            .tag(this)
                            .cacheKey("weatherCacheKey")
                            .cacheMode(CacheMode.DEFAULT)
                            .execute(new WeatherCallBack(){
                                @Override
                                public void onSuccess(WeatherEntity entity, Call call, Response response) {
                                    if(entity!=null){
                                        MyLog.d("data",entity.toString());
                                    }
                                }
                            });
                }catch (Exception e){
                    e.printStackTrace();
                    MyLog.d("异常了",e.toString());
                }
                break;
            case R.id.tv_get_weather_data_2:
                ToastUtils.showSingleTextToast(MyApplication.getInstance(),"Click");
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
