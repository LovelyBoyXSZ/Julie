package com.vincent.julie.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.vincent.julie.R;


/**
 * 获取验证码的倒计时
 * Created by Vincent on 2016/9/6.
 */

public class TimeCount extends CountDownTimer {

    private TextView tv;
    private Context c;
    private String text;

    /**
     * 初始化，构造函数
     * @param context 上下文对象
     * @param millisInFuture 总的时间
     * @param countDownInterval 一般设置为1000，一秒钟减1
     * @param textView 需要设置倒计时的对象
     * @param text 时间结束之后变成什么字
     */
    public TimeCount(Context context, long millisInFuture, long countDownInterval, TextView textView,String text) {
        super(millisInFuture, countDownInterval);
        this.tv = textView;
        this.c=context;
        this.text=text;
    }

    @Override
    public void onTick(long l) {//计时开始
        tv.setClickable(false);//设置不可点击
        tv.setTextColor(c.getResources().getColor(R.color.common_color_red));
        tv.setText(l / 1000 + "s");
    }

    @Override
    public void onFinish() {
        tv.setText(text);
        tv.setClickable(true);
    }

}
