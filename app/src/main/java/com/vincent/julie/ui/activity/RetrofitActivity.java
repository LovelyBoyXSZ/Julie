package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.retrofit.WeatherApiService;
import com.vincent.julie.retrofit.model.WeatherEntity;
import com.vincent.julie.retrofit.response.GetWeatherData;
import com.vincent.julie.util.ToastUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：使用Retrofit网络框架访问天气数据
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/17 16:27
 * 修改人：
 * 修改时间：
 * 修改备注： demo:http://download.csdn.net/download/liuhongwei123888/9372394
 * 十月
 */

public class RetrofitActivity extends BaseActivity {

    private static final String SERVICE_URI = "http://v.juhe.cn/weather/index?format=2&cityname=深圳&key=77a262c554de40916edc78858221b4a9";
    private static final String ENDPOINT = "http://v.juhe.cn";
    private WeatherEntity weatherEntity;

    @BindView(R.id.btn_get_weather)
    Button btnGetWeather;
    @BindView(R.id.tv_show_weather)
    TextView tvShowWeather;
    @BindView(R.id.rl_loading)
    RelativeLayout rlLoading;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==0){
                WeatherEntity weatherEntity=(WeatherEntity) msg.obj;
                tvShowWeather.setText(weatherEntity.getResult().getToday().getWeek());
            }
        }
    };

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_retrofit);
        setTitleText("Retrofit网络请求框架学习");


    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_weather)
    public void onClick() {
//        getData();
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



    }

//    private void getData() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);
//        weatherApiService.getWeatherData("2", "深圳", "77a262c554de40916edc78858221b4a9")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<GetWeatherData>() {
//                    @Override
//                    public void onCompleted() {
//                        rlLoading.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        rlLoading.setVisibility(View.GONE);
//                        tvShowWeather.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(GetWeatherData getWeatherData) {
//                        MyLog.e("getWeaatherData","==>"+getWeatherData.toString());
//                        WeatherEntity weatherEntity=getWeatherData.data;
//                        String status=weatherEntity.resultcode;
//                        if(status.equals("200")){
//                            if(getWeatherData.data!=null){
//                                MyLog.d("weather data-->",getWeatherData.data.toString());
//                                tvShowWeather.setText(status);
//                            }
//                        }else {
//                            ToastUtils.showSingleTextToast(MyApplication.getInstance(),"数据获取失败");
//                            MyLog.d("weather api","数据获取失败");
//                        }
//                    }
//                });
//    }
}
