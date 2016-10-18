package com.vincent.julie.okgo;

import com.lzy.okgo.callback.AbsCallback;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.retrofit.model.WeatherEntity;
import com.vincent.julie.util.ToastUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.okgo
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/18 14:05
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class WeatherCallBack<WeathEntity> extends AbsCallback<WeatherEntity> {


    @Override
    public void onSuccess(WeatherEntity entity, Call call, Response response) {
        if(response.equals("200")){
            MyLog.d("response","响应成功");
        }
    }

    /**
     * 此方法拿到相应的结果转为响应的对象
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public WeatherEntity convertSuccess(Response response) throws Exception {
        return null;
    }
}
