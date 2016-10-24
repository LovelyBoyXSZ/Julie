package com.vincent.julie.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;

import com.vincent.julie.R;
import com.vincent.julie.app.AppConstants;
import com.vincent.julie.app.BaseActivity;

/**
 * @name Julie
 * @class name：com.vincent.julie.ui.activity
 * @class describe
 * @anthor Vincent QQ:1032006226
 * @time 2016/10/24 11:47
 * @change
 * @chang time
 * @class describe
 */
public class TestActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.ctl_collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("卧槽草草草草...");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.common_color_red));
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);//控制方向，顶部title动画方向
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        ImageView backdrop = (ImageView) findViewById(R.id.iv_title_bar);
        //设置过渡动画
        ViewCompat.setTransitionName(backdrop, AppConstants.TRANSITION_PIC);
    }
}
