package com.vincent.julie.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.util.AnimationUtils;
import com.vincent.julie.util.ToastUtils;


/**
 * Created by caik on 2016/9/25.
 */

public class BottomDialog extends DialogFragment {

    public static BottomDialog newInstance() {

        Bundle args = new Bundle();

        BottomDialog fragment = new BottomDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_bottom,container,false);
        TextView textView= (TextView) view.findViewById(R.id.tv_item);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showSingleTextToast(MyApplication.getInstance(),"dialog dismiss");
                getDialog().dismiss();
            }
        });
        AnimationUtils.slideToUp(view);
        return view;
    }


}
