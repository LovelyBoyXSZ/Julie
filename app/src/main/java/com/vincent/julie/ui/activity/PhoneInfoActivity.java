package com.vincent.julie.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.entity.PhoneInfo;
import com.vincent.julie.listener.PhoneInfoListener;
import com.vincent.julie.ui.adapter.PhoneInfoAdapter;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;
import com.vincent.julie.view.DividerItemDecoration;
import com.vincent.julie.view.ThreeDLayout;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * 获取手机相关信息
 *3 D特效：https://github.com/githubwing/ThreeDLayout/blob/master/README_CN.md
 * @author Vincent QQ1032006226
 *         created at 2016/9/27 15:24
 */
@RuntimePermissions
public class PhoneInfoActivity extends BaseActivity {
    private ThreeDLayout dLayout;
    private TextView tvStartAnim;

    private RecyclerView rlvContent;
    private List<PhoneInfo> data;
    private PhoneInfoAdapter adapter;
    private boolean flag=true;


    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_phone_info);
        setTitleText("设备信息");
        data=new ArrayList<>();
        adapter=new PhoneInfoAdapter();
        //TODO 判断手指滑动的方向
        dLayout = (ThreeDLayout) findViewById(R.id.tdl_threed);
        rlvContent=(RecyclerView)findViewById(R.id.rlv_content);
        rlvContent.setItemAnimator(new DefaultItemAnimator());//默认动画效果
        rlvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));//设置布局管理器，第三个参数为是否逆向布局
        rlvContent.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL)); //分割线
        rlvContent.setHasFixedSize(true);

        //开启触摸模式
//        dLayout.setTouchable(true);
        //改变触摸模式
//        dLayout.setTouchMode(ThreeDLayout.MODE_BOTH_X_Y);
        ////开始执行动画
        //startVerticalAnimate( long duration);
        //startVerticalAnimateDelayed( final long delayed, final long duration)
        //startHorizontalAnimate( long duration)
        //startHorizontalAnimateDelayed( final long delayed, final long duration)
        //开启循环动画
        //startHorizontalAnimate()
        ////关闭循环动画
        //stopAnimate()
        tvStartAnim=(TextView)findViewById(R.id.tv_start_anim);
        tvStartAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    flag=false;
                    ToastUtils.showSingleTextToast(MyApplication.getInstance(),"动画开始啦");
                }else {
                    flag=true;
                    ToastUtils.showSingleTextToast(MyApplication.getInstance(),"动画又开始啦");
                }
                dLayout.startHorizontalAnimate(3000);
            }
        });
        setData();
        adapter.setData(data);
        adapter.setPhoneInfoListener(new PhoneInfoListener() {
            @Override
            public void onClick(View view, int postion) {
                ToastUtils.showSingleTextToast(MyApplication.getInstance(),data.get(postion).getName()+","+data.get(postion).getInfo());
            }
        });
        rlvContent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setData() {
        PhoneInfoActivityPermissionsDispatcher.getSystemDataWithCheck(PhoneInfoActivity.this);
    }


    @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void getSystemData() {
//        ToastUtils.showSingleTextToast(MyApplication.getInstance(),SystemUtilts.getPhoneManufacturer());
        PhoneInfo phoneInfo=new PhoneInfo();
        phoneInfo.setName("手机厂商");
        phoneInfo.setInfo(SystemUtilts.getPhoneManufacturer());
//        phoneInfo.setInfo("华为");
        data.add(phoneInfo);

        PhoneInfo phoneInfo1=new PhoneInfo();
        phoneInfo.setName("手机型号");
        phoneInfo.setInfo(SystemUtilts.getPhoneMdel());
        data.add(phoneInfo1);

        PhoneInfo phoneInfo2=new PhoneInfo();
        phoneInfo.setName("sdk版本");
        phoneInfo.setInfo(SystemUtilts.getAndroidSDKVersionStr());
        data.add(phoneInfo2);

        PhoneInfo phoneInfo3=new PhoneInfo();
        phoneInfo.setName("系统版本");
        phoneInfo.setInfo(SystemUtilts.getVersionOs());
        data.add(phoneInfo3);

        PhoneInfo phoneInfo4=new PhoneInfo();
        phoneInfo.setName("cpu信息");
        phoneInfo.setInfo(SystemUtilts.getCpuName());
        data.add(phoneInfo4);

        PhoneInfo phoneInfo5=new PhoneInfo();
        phoneInfo.setName("基带版本");
        phoneInfo.setInfo(SystemUtilts.getBaseband_Ver());
        data.add(phoneInfo5);

        PhoneInfo phoneInfo6=new PhoneInfo();
        phoneInfo.setName("内核版本");
        phoneInfo.setInfo(SystemUtilts.getLinuxCore_Ver());
        data.add(phoneInfo6);

        PhoneInfo phoneInfo7=new PhoneInfo();
        phoneInfo.setName("内部版本");
        phoneInfo.setInfo(SystemUtilts.getInner_Ver());
        data.add(phoneInfo7);

        PhoneInfo phoneInfo8=new PhoneInfo();
        phoneInfo.setName("手机号码");
        phoneInfo.setInfo(SystemUtilts.getPhoneNumber(PhoneInfoActivity.this));
        data.add(phoneInfo8);

        PhoneInfo phoneInfo9=new PhoneInfo();
        phoneInfo.setName("屏幕宽度");
        phoneInfo.setInfo(String.valueOf(MyApplication.getScreenParameterWidth()));
        data.add(phoneInfo9);

        PhoneInfo phoneInfo10=new PhoneInfo();
        phoneInfo.setName("屏幕高度");
        phoneInfo.setInfo(String.valueOf(MyApplication.getScreenParameterHeight()));
        data.add(phoneInfo10);

        PhoneInfo phoneInfo11=new PhoneInfo();
        phoneInfo.setName("手机运行内存");
        phoneInfo.setInfo(SystemUtilts.getTotalMemory(PhoneInfoActivity.this) + "G");
        data.add(phoneInfo11);

        PhoneInfo phoneInfo12=new PhoneInfo();
        phoneInfo.setName("手机可用运行内存");
        phoneInfo.setInfo(SystemUtilts.getPhoneRAM(PhoneInfoActivity.this));
        data.add(phoneInfo12);

        PhoneInfo phoneInfo13=new PhoneInfo();
        phoneInfo.setName("imei号码");
        phoneInfo.setInfo(SystemUtilts.getIMEINumber(PhoneInfoActivity.this));
        data.add(phoneInfo13);

        PhoneInfo phoneInfo14=new PhoneInfo();
        phoneInfo.setName("手机内存");
        phoneInfo.setInfo(SystemUtilts.getMemFree(PhoneInfoActivity.this));
        data.add(phoneInfo14);

        PhoneInfo phoneInfo15=new PhoneInfo();
        phoneInfo.setName("内置SD卡总大小");
        phoneInfo.setInfo(SystemUtilts.getSDSize(PhoneInfoActivity.this));
        data.add(phoneInfo15);

        PhoneInfo phoneInfo17=new PhoneInfo();
        phoneInfo.setName("系统可用内存");
        phoneInfo.setInfo(SystemUtilts.getPhoneRAM(PhoneInfoActivity.this));
        data.add(phoneInfo17);

        PhoneInfo phoneInfo18=new PhoneInfo();
        phoneInfo18.setName("是否Root");
        if(SystemUtilts.isRoot()){
            phoneInfo18.setInfo("是");
        }else {
            phoneInfo18.setInfo("否");
        }
        data.add(phoneInfo18);

    }



    @OnShowRationale({Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void showRationaleForGetSystemData(PermissionRequest request) {
        showRationaleDialog("获取手机相关信息需要申请相关权限，否则将获取不到信息", request);
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void getSystemDateDenied() {
        ToastUtils.showSingleTextToast(this, "你拒绝了权限，该功能不可用");
    }

    @OnNeverAskAgain({Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void getSystemDateNeverAskAgain() {
        ToastUtils.showSingleTextToast(this, "不再允许询问该权限，该功能不可用");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PhoneInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
