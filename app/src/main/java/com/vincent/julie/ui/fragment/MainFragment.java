package com.vincent.julie.ui.fragment;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
  *主页
  *@author Vincent QQ1032006226
  *created at 2016/9/26 15:54
  */

public class MainFragment extends BackHandledFragment {
    private View view;
    private boolean isQuit=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.main_frg_main,null);
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
