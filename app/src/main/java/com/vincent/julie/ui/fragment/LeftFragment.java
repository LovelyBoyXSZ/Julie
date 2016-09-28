package com.vincent.julie.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.julie.R;

/**
 * Created by Vincent on 2016/9/26.
 */
/**
  * 第二个fragment
  *@author Vincent QQ1032006226
  *created at 2016/9/26 15:54
  */
public class LeftFragment extends BackHandledFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.main_frg_left,null);
        }
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
