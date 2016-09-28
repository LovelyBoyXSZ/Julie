package com.vincent.julie.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.julie.R;

/**
  * 第三个Fragment
  *@author Vincent QQ1032006226
  *created at 2016/9/26 15:54
  */

public class CenterFragment extends BackHandledFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.main_frg_center,null);
        }
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
