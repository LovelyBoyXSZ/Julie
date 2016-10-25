package com.vincent.julie.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vincent.julie.R;
import com.vincent.julie.ui.activity.InternetActivity;
import com.vincent.julie.util.SystemUtilts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 15:54
 */

public class MainFragment extends BackHandledFragment {

    @BindView(R.id.btn_go_permission_act)
    Button btnGoPermissionAct;
    @BindView(R.id.btn_go_windows_permission)
    Button btnGoWindowsPermission;
    @BindView(R.id.btn_go_protect_act)
    Button btnGoProtectAct;
    @BindView(R.id.btn_go_self_motion_start)
    Button btnGoSelfMotionStart;
    @BindView(R.id.btn_relevance_start)
    Button btnRelevanceStart;
    @BindView(R.id.btn_go_notification_manager)
    Button btnNotificationMainager;
    @BindView(R.id.btn_go_clear_memory)
    Button btnCliearMemory;
    @BindView(R.id.btn_go_interception)
    Button btnInterception;
    @BindView(R.id.btn_test)
    Button btnTestActivity;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frg_main, null);
        }
        ButterKnife.bind(this, view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onBackPressed() {
        return false;
    }

    @OnClick({R.id.btn_go_permission_act, R.id.btn_go_windows_permission,R.id.btn_go_notification_manager,
            R.id.btn_go_protect_act, R.id.btn_go_self_motion_start,R.id.btn_relevance_start,
            R.id.btn_go_clear_memory,R.id.btn_go_interception,R.id.btn_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_permission_act:
                SystemUtilts.goHWWindowPermission();
                break;
            case R.id.btn_go_windows_permission:
                SystemUtilts.goHuaWeiSetting();
                break;
            case R.id.btn_go_protect_act:
                SystemUtilts.goProtectAppManager();
                break;
            case R.id.btn_go_self_motion_start:
                SystemUtilts.goHWSelfMotionStartManager();
                break;
            case R.id.btn_relevance_start:
                SystemUtilts.goRelevanceStartManager();
                break;
            case R.id.btn_go_notification_manager:
                SystemUtilts.goNotificationManager();
                break;
            case R.id.btn_go_clear_memory:
                SystemUtilts.goClearMemory();
                break;
            case R.id.btn_go_interception:
                SystemUtilts.goInterceptionAct();
//                SystemUtilts.goXiaoMiPermissionManagerActivity(getContext(),"打开失败");
                break;
            case R.id.btn_test:
                Intent intent=new Intent(getActivity(), InternetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
