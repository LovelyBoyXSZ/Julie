package com.vincent.julie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.ui.activity.JiGuangPushActivity;
import com.vincent.julie.ui.activity.OkGoActivity;
import com.vincent.julie.ui.activity.PhoneInfoActivity;
import com.vincent.julie.ui.activity.QrScodeUtilsActivity;
import com.vincent.julie.ui.activity.RetrofitActivity;
import com.vincent.julie.ui.activity.RxJavaTestActivity;
import com.vincent.julie.ui.activity.SettingActivity;
import com.vincent.julie.ui.activity.TestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 最后一个fragment
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 15:54
 */

public class RightFragmet extends BackHandledFragment {
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.ll_mainfrg_phone_info)
    LinearLayout llMainfrgPhoneInfo;
    @BindView(R.id.ll_mainfrg_setting)
    LinearLayout llMainfrgSetting;
    @BindView(R.id.ll_mainfrg_qr_code)
    LinearLayout llMainfrgQrCode;
    @BindView(R.id.ll_rxjava)
    LinearLayout llRxJava;
    @BindView(R.id.tv_retrofit)
    TextView tvRetrofit;
    @BindView(R.id.tv_okgo)
    TextView tvOkGo;
    @BindView(R.id.tv_jiguang_push)
    TextView tvJiGuangPush;
    @BindView(R.id.tv_magnify)
    TextView tvMagnify;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frg_right, null);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @OnClick({R.id.ll_mainfrg_phone_info, R.id.ll_mainfrg_setting,R.id.ll_mainfrg_qr_code,
    R.id.ll_rxjava,R.id.tv_retrofit,R.id.tv_okgo,R.id.tv_jiguang_push,R.id.tv_magnify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mainfrg_phone_info:
                Intent intent = new Intent(getActivity(), PhoneInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.ll_mainfrg_setting:
                Intent intent2 = new Intent(getActivity(), SettingActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            case R.id.ll_mainfrg_qr_code:
                Intent intent1=new Intent(getActivity(), QrScodeUtilsActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.ll_rxjava:
                Intent rxJava=new Intent(getActivity(), RxJavaTestActivity.class);
                rxJava.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(rxJava);
                break;
            case R.id.tv_retrofit:
                Intent retrofit=new Intent(getActivity(), RetrofitActivity.class);
                retrofit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(retrofit);
                break;
            case R.id.tv_okgo:
                Intent okgo=new Intent(getActivity(), OkGoActivity.class);
                okgo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(okgo);
                break;
            case R.id.tv_jiguang_push:
                Intent jiGuang=new Intent(getActivity(), JiGuangPushActivity.class);
                jiGuang.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(jiGuang);
                break;
            case R.id.tv_magnify:
                Intent magnify=new Intent(getActivity(), TestActivity.class);
                magnify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(magnify);
                break;
            default:
                break;
        }
    }
}
