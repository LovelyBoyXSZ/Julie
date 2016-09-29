package com.vincent.julie.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.listener.BackHandledInterface;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.service.JulieService;
import com.vincent.julie.ui.fragment.BackHandledFragment;
import com.vincent.julie.ui.fragment.CenterFragment;
import com.vincent.julie.ui.fragment.LeftFragment;
import com.vincent.julie.ui.fragment.MainFragment;
import com.vincent.julie.ui.fragment.RightFragmet;
import com.vincent.julie.util.AppUtil;
import com.vincent.julie.util.FragmentSwitchTool;
import com.vincent.julie.util.StatusBarUtil;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;
import com.vincent.julie.view.SlidingMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 8:37
 */
public class MainActivity extends FragmentActivity implements BackHandledInterface {
    private LinearLayout llChat, llFriends, llContacts, llSettings;
    private ImageView ivChat, ivFriends, ivContacts, ivSettings;
    private TextView tvChat, tvFriends, tvContacts, tvSettings;
    private BackHandledFragment mBackHandledFragment;
    private List<Fragment> frgList = new ArrayList<>();
    private FragmentSwitchTool tool;

    private boolean isQuit = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.act_main);
        MyApplication.addActivity(this);
        initViews();
        startService();
        tool = new FragmentSwitchTool(getSupportFragmentManager(), R.id.flContainer);
        tool.setClickableViews(llChat, llFriends, llContacts, llSettings);
        tool.addSelectedViews(new View[]{ivChat, tvChat}).addSelectedViews(new View[]{ivFriends, tvFriends})
                .addSelectedViews(new View[]{ivContacts, tvContacts}).addSelectedViews(new View[]{ivSettings, tvSettings});
        tool.setFragments(MainFragment.class, LeftFragment.class, CenterFragment.class, RightFragmet.class);
        tool.changeTag(llChat);
    }
    /**
     * 此方法配置顶部状态栏颜色
     */
    private void setStatusBar() {
        if (SystemUtilts.getAndroidSDKVersion() > 19 || SystemUtilts.getAndroidSDKVersion() == 19) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.common_color_green), 1);//int类型的值控制透明度
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.common_color_green));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActiivty(this);
    }

    /**
     * 启动service
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/27 9:36
     */
    private void startService() {
        if (!AppUtil.isServiceRunning(MainActivity.this, "com.vincent.julie.service.JulieService")) {
            MyLog.d(MainActivity.class.getSimpleName(), "start JulieService");
            startService(new Intent(MainActivity.this, JulieService.class));
        } else {
            MyLog.d(MainActivity.class.getSimpleName(), "JulieService is runing");
        }
    }

    /**
     * 设置只有MainFragment才能退出app
     * @author Vincent QQ1032006226
     * created at 2016/9/27 11:09
     */
    @Override
    public void onBackPressed() {
        if (tool.getCurrentFragment() instanceof MainFragment) {
            MyLog.d("MainActivity", "当前Fragment是MainFragment，处理返回键事件");
            if (isQuit == false) {
                isQuit = true;
                ToastUtils.showDefaultToastCenter(getApplicationContext(), "再按一次离开app");
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);
            } else {
                MyApplication.removeAllActivity();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                Log.d("Conversatio退出", "Conversatio退出");
            }
        } else {
            MyLog.d("MainActivity", "当前Fragment不是MainFragment，跳转到MainFragment");
            tool.changeTag(llChat);
            getSupportFragmentManager().popBackStack();
        }
    }

    private void initViews() {
        llChat = (LinearLayout) findViewById(R.id.llChat);
        llFriends = (LinearLayout) findViewById(R.id.llFriends);
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        llSettings = (LinearLayout) findViewById(R.id.llSettings);

        ivChat = (ImageView) findViewById(R.id.ivChat);
        ivFriends = (ImageView) findViewById(R.id.ivFriends);
        ivContacts = (ImageView) findViewById(R.id.ivContacts);
        ivSettings = (ImageView) findViewById(R.id.ivSettings);

        tvChat = (TextView) findViewById(R.id.tvChat);
        tvFriends = (TextView) findViewById(R.id.tvFriends);
        tvContacts = (TextView) findViewById(R.id.tvContacts);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
    }


    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandledFragment = selectedFragment;
    }
}
