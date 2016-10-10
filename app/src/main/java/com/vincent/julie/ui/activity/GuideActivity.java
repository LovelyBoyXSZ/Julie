package com.vincent.julie.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;


/**
 * 引导页
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 8:57
 */
public class GuideActivity extends BaseActivity {

    // 显示导航页面的viewpager
    private ViewPager guideViewPager;

    // 页面适配器
    private ViewPagerAdapter guideViewAdapter;

    // 页面图片列表
    private ArrayList<View> mViews;

    // 图片资源，这里我们放入了3张图片，因为第四张图片，我们已经在guide_content_view.xml中加载好了
    // 一会直接添加这个文件就可以了。
    private final int images[] = {
            R.drawable.icon_welcom,  R.drawable.icon_welcom,  R.drawable.icon_welcom
    };

    // 底部导航的小点
    private ImageView[] guideDots;

    // 记录当前选中的图片
    private int currentIndex;

    // 还记得我们的开始按钮吗？
    private Button startBtn;

    private TextView ivGoMain;


    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_guide);
        hideTitle();//隐藏顶部title
        initGuideView();
        initDot();
        // 添加页面更换监听事件，来更新导航小点的状态。
        guideViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                setCurrentDot(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void initDot() {
        // 找到放置小点的布局
        LinearLayout layout = (LinearLayout) findViewById(R.id.guide_dots);

        // 初始化小点数组
        guideDots = new ImageView[mViews.size()];

        // 循环取得小点图片，让每个小点都处于正常状态
        for (int i = 0; i < mViews.size(); i++) {
            guideDots[i] = (ImageView) layout.getChildAt(i);
            guideDots[i].setSelected(false);
        }

        // 初始化第一个小点为选中状态
        currentIndex = 0;
        guideDots[currentIndex].setSelected(true);
    }

    private void initGuideView() {
        ivGoMain = (TextView) findViewById(R.id.iv_go_main);
        guideViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        mViews = new ArrayList<View>();

        for (int i = 0; i < images.length; i++) {
            // 新建一个ImageView容器来放置我们的图片。
            ImageView iv = new ImageView(GuideActivity.this);
            iv.setBackgroundResource(images[i]);

            // 将容器添加到图片列表中
            mViews.add(iv);
        }

        // 上面添加了三张图片了，还有一张放在guide_content_view.xml中，我们把这个页面也添加进来。
        View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_content_view,
                null);
        mViews.add(view);

        // 现在为我们的开始按钮找到对应的控件
        startBtn = (Button) view.findViewById(R.id.start_btn);

        // 现在用到我们的页面适配器了
        guideViewAdapter = new ViewPagerAdapter(mViews);

        guideViewPager.setAdapter(guideViewAdapter);
    }

    // 页面更换时，更新小点状态
    private void setCurrentDot(int position) {
        if (position < 0 || position > mViews.size() - 1 || currentIndex == position) {
            return;
        }
        guideDots[position].setSelected(true);
        guideDots[currentIndex].setSelected(false);

        currentIndex = position;
        if(position==3){
            if(ivGoMain!=null)
                ivGoMain.setVisibility(View.GONE);
        }else {
            if(ivGoMain!=null)
                ivGoMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                //跳转到主页
                goLogin();
                break;
            case R.id.iv_go_main:
//                goRegister();
                goMain();
                break;
            default:
        }
    }

    private void goLogin() {
        startActivity(new Intent(GuideActivity.this,MainActivity.class));
        finish();
    }

    private void goMain() {
        startActivity(new Intent(GuideActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void setListener() {
        startBtn.setOnClickListener(this);
        ivGoMain.setOnClickListener(this);
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_HOME:return true;
            case KeyEvent.KEYCODE_BACK:return true;
            case KeyEvent.KEYCODE_CALL:return true;
            case KeyEvent.KEYCODE_SYM: return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
            case KeyEvent.KEYCODE_VOLUME_UP: return true;
            case KeyEvent.KEYCODE_STAR: return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

