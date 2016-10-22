package com.vincent.julie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.service.MyIntentService;
import com.vincent.julie.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 2016/9/26.
 */

/**
 * 第二个fragment
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 15:54
 */
public class LeftFragment extends BackHandledFragment {
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_start_other_activity)
    Button btnStartOtherActivity;
    @BindView(R.id.start_myintentservice)
    Button startMyintentservice;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frg_left, null);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @OnClick({R.id.btn_start_other_activity,R.id.start_myintentservice})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_other_activity:
                AppUtil.startOtherAppActivity("com.shangyi.supplier", "com.shangyi.ui.activity.SplashActivity");
                break;
            case R.id.start_myintentservice:
                MyLog.w(LeftFragment.class.getSimpleName(),"启动MyIntentService");
                MyApplication.getInstance().startService(new Intent(getActivity(),MyIntentService.class));
                break;
            default:
                break;
        }

    }

}
