package com.vincent.julie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.view.AnFQNumEditText;
import com.vincent.julie.view.BottomDialog;

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
    @BindView(R.id.tv_send_broadcast_receiver)
    TextView tvSendBroadcastReceiver;

    private View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frg_center, null);
        }
        ButterKnife.bind(this, view);
        SystemUtilts.getReflectInstance(getContext(), "com.vincent.julie.entity.Food");
        System.out.print("--------------------");
//        SystemUtilts.getReflectInstance(getContext(), "com.huawei.systemmanager.mainscreen.MainScreenActivity");

        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @OnClick({R.id.tv_choose_img,R.id.tv_send_broadcast_receiver})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_choose_img:
                //在底部弹出一个dialog
                BottomDialog bottomDialog = BottomDialog.newInstance();
                bottomDialog.show(getFragmentManager(), BottomDialog.class.getSimpleName());
                break;
            case R.id.tv_send_broadcast_receiver:
                Intent intent=new Intent("custom_broadcast_receiver");
                MyApplication.getInstance().sendBroadcast(intent);
                break;
        }
    }

}
