package com.vincent.julie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 第三个Fragment
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 15:54
 */

public class CenterFragment extends BackHandledFragment {

    @BindView(R.id.tv_choose_img)
    TextView tvChooseImg;
    @BindView(R.id.iv_show_img)
    ImageView ivShowImg;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frg_center, null);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @OnClick(R.id.tv_choose_img)
    public void onClick() {
        ToastUtils.showSingleTextToast(MyApplication.getInstance(),"click");
    }
}
