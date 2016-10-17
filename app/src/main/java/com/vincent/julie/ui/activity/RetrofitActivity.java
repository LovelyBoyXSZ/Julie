package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.retrofit.WeatherApiService;
import com.vincent.julie.retrofit.response.GetWeatherData;
import com.vincent.julie.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：
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


    @BindView(R.id.btn_get_weather)
    Button btnGetWeather;
    @BindView(R.id.tv_show_weather)
    TextView tvShowWeather;
    @BindView(R.id.rl_loading)
    RelativeLayout rlLoading;

    //  SERVICE_URI=http://v.juhe.cn/weather/index?format=2&cityname=深圳&key=77a262c554de40916edc78858221b4a9

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

        weatherApiService.getWeatherData("2", "深圳", "77a262c554de40916edc78858221b4a9")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetWeatherData>() {
                    @Override
                    public void onCompleted() {
                        rlLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoading.setVisibility(View.GONE);
                        ToastUtils.showSingleTextToast(MyApplication.getInstance(), e.getMessage());
                    }

                    @Override
                    public void onNext(GetWeatherData getIpInfoResponse) {
                        rlLoading.setVisibility(View.GONE);
                        tvShowWeather.setText(getIpInfoResponse.data.reason);

                    }
                });

    }
}
