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
