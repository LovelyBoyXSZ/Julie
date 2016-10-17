package com.vincent.julie.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.ui.activity
 * 类描述：RxJava学习 Retrolambda插件的使用
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/17 10:15
 * 修改人：
 * 修改时间：
 * 修改备注：Retrolambda使用介绍：http://blog.csdn.net/cai_iac/article/details/50846139  Demo:https://github.com/evant/gradle-retrolambda
 * 如果你习惯使用 Retrolambda ，你也可以直接把代码写成上面这种简洁的形式。而如果你看到这里还不知道什么是 Retrolambda ，
 * 我不建议你现在就去学习它。原因有两点：1. Lambda 是把双刃剑，它让你的代码简洁的同时，降低了代码的可读性，因此同时学习 RxJava 和 Retrolambda 可能会让你忽略 RxJava 的一些技术细节；
 * 2. Retrolambda 是 Java 6/7 对 Lambda 表达式的非官方兼容方案，它的向后兼容性和稳定性是无法保障的，因此对于企业项目，使用 Retrolambda 是有风险的。
 * 所以，与很多 RxJava 的推广者不同，我并不推荐在学习 RxJava 的同时一起学习 Retrolambda。事实上，我个人虽然很欣赏 Retrolambda，但我从来不用它。
 * <p>
 * 十月
 */
public class RxJavaTestActivity extends BaseActivity {
    private static final String  OBSERVER_TAG="observer";
    @BindView(R.id.tv_retrolambda_test)
    TextView tvRetrolambdaTest;
    @BindView(R.id.tv_txjava_test)
    TextView tvTxjavaTest;
    @BindView(R.id.iv_test_rxjava)
    ImageView ivTestRxjava;

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_rxjava);
        setTitleText("RxJava学习");
        //使用了注解在这里写点击事件会报空指针，必须在OnCreate方法中写点击事件
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        /* tvRetrolambdaTest.setOnClickListener(new View.OnClickListener() {//使用Retrolambda插件之前的写法
            @Override
            public void onClick(View v) {
                ToastUtils.showSingleTextToast(RxJavaTestActivity.this,"Retrolambda学习，弹出来一个Toast");
            }
        });*/
        //使用Retrolambda插件之后的写法
        tvRetrolambdaTest.setOnClickListener(view -> ToastUtils.showSingleTextToast(this, "Retrolambda学习，弹出来一个Toast"));

        /**
         * 在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，并且是事件序列中的最后一个。
         * 需要注意的是，onCompleted() 和 onError() 二者也是互斥的，即在队列中调用了其中一个，就不应该再调用另一个。
         */
        Observer<String> observer=new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Observable 和 Observer 通过 subscribe() 方法实现订阅关系，
                // 从而 Observable 可以在需要的时候发出事件来通知 Observer。
                //相当于普通模式里面的setOnClicklistener
                MyLog.d(OBSERVER_TAG,"onSubscribe()方法调用了");


            }

            @Override
            public void onNext(String value) {
                //相当于 new OnClickListener里面的onClick() / onEvent()
                MyLog.d(OBSERVER_TAG,"onNext（）方法调用了");
            }

            @Override
            public void onError(Throwable e) {
                //事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。
                MyLog.d(OBSERVER_TAG,"onError（）方法调用了");
            }

            @Override
            public void onComplete() {
                // 事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。
                // RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志
                MyLog.d(OBSERVER_TAG,"");
            }
        };
    }


    @OnClick({R.id.tv_txjava_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_txjava_test:

                break;
            default:
                break;
        }
    }

}
