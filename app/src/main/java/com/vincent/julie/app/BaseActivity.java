package com.vincent.julie.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.StatusBarUtil;
import com.vincent.julie.util.SystemUtilts;

/**
 * Created by Vincent on 2016/9/26.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private LinearLayout mDefTitleBarRootView;//外层的根View
    private RelativeLayout rlTitle;//title

    private RelativeLayout rlLeftReturn;//左边的返回按钮
    private ImageView ivLeftImg;//左边的返回是图片
    private TextView tvLeftText;//左边的返回是文字

    private TextView tvTitleText;//中间的文字

    private RelativeLayout rlRightSetImg;//右边的设置按钮
    private ImageView ivRightImg;//左边的返回是图片
    private TextView tvRightText;//左边的返回是文字

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d(TAG, "onCreate()");
        getSupportActionBar().hide();//隐藏ActionBar
        MyApplication.addActivity(this);
        setStatusBar();
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.act_root);
        LinearLayout rootView = (LinearLayout) this.findViewById(R.id.ll_root);
        View titleBar = createTitleBar();
        if (titleBar != null) {
            rootView.addView(titleBar);
        }
        try {
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(view, lp);
        } catch (Exception e) {
            MyLog.e(TAG, " add view error :" + e);
        }
    }

    /**
     * 创建titleBar视图,如需自定义titlebar，则覆盖此重写此方法
     *
     * @return
     */
    protected View createTitleBar() {
        View view = null;
        try {
            view = LayoutInflater.from(this).inflate(R.layout.act_common_title, null);
            initDefaultTitleBar(view);
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e(TAG, " create base titlebar error:" + e.toString());
        }
        return view;
    }

    /**
     * 初始化titleBar
     */
    private void initDefaultTitleBar(View view) {
        MyLog.d(TAG, " init default titlebar ...");
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.common_color_green));
            mDefTitleBarRootView = (LinearLayout) view.findViewById(R.id.common_title_root_rl);
            if (mDefTitleBarRootView != null) {
                rlTitle = (RelativeLayout) mDefTitleBarRootView.findViewById(R.id.rl_title);

                rlLeftReturn = (RelativeLayout) mDefTitleBarRootView.findViewById(R.id.common_title_left_rl);
                rlLeftReturn.setOnClickListener(mTitleBarListener);
                tvLeftText = (TextView) mDefTitleBarRootView.findViewById(R.id.tv_left_text);
                ivLeftImg = (ImageView) mDefTitleBarRootView.findViewById(R.id.iv_title_img_return);
//
                tvTitleText = (TextView) mDefTitleBarRootView.findViewById(R.id.tv_common_title);

                rlRightSetImg=(RelativeLayout)findViewById(R.id.common_title_right); //默认是不显示的
                ivRightImg = (ImageView) mDefTitleBarRootView.findViewById(R.id.iv_setting);
                tvRightText = (TextView) mDefTitleBarRootView.findViewById(R.id.tv_title_right_text);
            }
        }
    }
    /**
      *设置顶部title的文字
      *@author Vincent QQ1032006226
      *created at 2016/9/26 11:35
      */
    public void setTitleText(String text){
        tvTitleText.setText(text);
    }
    /**
      *默认右边
      *@author Vincent QQ1032006226
      *created at 2016/9/26 11:31
      */
    public void showTitleRight(){
        rlRightSetImg.setVisibility(View.VISIBLE);
    }
    /**
      *右边部分显示为文字
      *@author Vincent QQ1032006226
      *created at 2016/9/26 11:33
      */
    public void showTitleRightIsText(String rightText){
        rlRightSetImg.setVisibility(View.VISIBLE);
        ivRightImg.setVisibility(View.GONE);
        tvRightText.setText(rightText);
        tvRightText.setVisibility(View.VISIBLE);
    }

    /**
      *title左边部分是文字
      *@author Vincent QQ1032006226
      *created at 2016/9/26 11:29
      */
    public void titleLeftIsText(String text){
        tvLeftText.setText(text);
    }

    /**
      *调用此方法隐藏底部title
      *@author Vincent QQ1032006226
      *created at 2016/9/26 11:28
      */
    public void hideTitle(){
        rlTitle.setVisibility(View.GONE);
    }


    /**
     * 默认titlebar左右两边按钮事件监听
     */
    private View.OnClickListener mTitleBarListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.common_title_left_rl:
                    titleBarBackClick();
                    break;
                case R.id.common_title_right:
                    titleBarRightClick();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 点击顶部title右边部分调用的方法
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 11:25
     */
    protected abstract void titleBarRightClick();

    /**
     * 结束activity
     *
     * @author Vincent QQ1032006226
     * created at 2016/9/26 11:11
     */
    private void titleBarBackClick() {
        finish();
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

    private void initView() {
        findViewById();
        setListener();
    }

    /**
     * 查找控件ID,务必在此方法首行setContentView加载布局
     */
    protected abstract void findViewById();

    /**
     * 控件的点击事件监听
     */
    protected abstract void setListener();

    @Override
    protected void onStart() {
        super.onStart();
        MyLog.d(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyLog.d(TAG, "onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyLog.d(TAG, "onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.d(TAG, "onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.d(TAG, "onDestroy()");
        MyApplication.removeActiivty(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.d(TAG, "onPause()");
    }
}
