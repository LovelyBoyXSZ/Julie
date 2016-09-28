package com.vincent.julie.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
  *获取手机相关信息
  *@author Vincent QQ1032006226
  *created at 2016/9/27 15:24
  */
@RuntimePermissions
public class PhoneInfoActivity extends BaseActivity {
    private ScrollView slvContent;
    private TextView tv_phone_manufacturer;//手机厂商
    private TextView tv_phone_model;//手机型号
    private TextView tv_sdk_version;//sdk版本
    private TextView tv_version_os;//系统版本
    private TextView tv_cpu_info;//cpu信息
    private TextView tv_base_band_version;//基带版本
    private TextView tv_core_version;//内核版本
    private TextView tv_interior_version;//内部版本
    private TextView tv_phone_number;//手机号码
    private TextView tv_screen_width;//屏幕宽度
    private TextView tv_screen_height;//屏幕高度
    private TextView tv_phone_all_ram;//手机运行内存
    private TextView tv_phone_ram;//手机可用运行内存
    private TextView tv_imei_number;//imei号码
    private TextView tv_phone_store;//手机内存
    private TextView tv_phone_store_interior;//内置SD卡总大小
    private TextView tv_phone_store_exterior_all;//外置SD卡
    private TextView tv_phone_resolution_ratio;//手机分辨率
    private TextView tv_phone_root;//当前手机是否有root权限

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_phone_info);
        setTitleText("设备信息");
        slvContent=(ScrollView)findViewById(R.id.sv_content);
        tv_phone_manufacturer=(TextView)findViewById(R.id.tv_phone_manufacturer);
        tv_phone_model=(TextView)findViewById(R.id.tv_phone_model);
        tv_sdk_version=(TextView)findViewById(R.id.tv_sdk_version);
        tv_cpu_info=(TextView)findViewById(R.id.tv_cpu_info);
        tv_core_version=(TextView)findViewById(R.id.tv_core_version);
        tv_interior_version=(TextView)findViewById(R.id.tv_interior_version);
        tv_base_band_version=(TextView)findViewById(R.id.tv_base_band_version);
        tv_version_os=(TextView)findViewById(R.id.tv_version_os);
        tv_phone_number=(TextView)findViewById(R.id.tv_phone_number);
        tv_screen_width=(TextView)findViewById(R.id.tv_screen_width);
        tv_screen_height=(TextView)findViewById(R.id.tv_screen_height);
        tv_phone_ram=(TextView)findViewById(R.id.tv_phone_ram);
        tv_phone_all_ram=(TextView)findViewById(R.id.tv_phone_all_ram);
        tv_imei_number=(TextView)findViewById(R.id.tv_imei_number);
        tv_phone_store=(TextView)findViewById(R.id.tv_phone_store);
        tv_phone_store_interior=(TextView)findViewById(R.id.tv_phone_store_interior);
        tv_phone_resolution_ratio=(TextView)findViewById(R.id.tv_phone_resolution_ratio);
        tv_phone_root=(TextView)findViewById(R.id.tv_phone_root);
        tv_phone_store_exterior_all=(TextView)findViewById(R.id.tv_store_exterior_all);

//        setData();
        PhoneInfoActivityPermissionsDispatcher.setDataWithCheck(PhoneInfoActivity.this);
    }

@NeedsPermission({Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE})
    void setData() {
        try {
            tv_phone_manufacturer.setText(SystemUtilts.getPhoneManufacturer());
            tv_phone_model.setText(SystemUtilts.getPhoneMdel());
            tv_sdk_version.setText(String.valueOf(SystemUtilts.getAndroidSDKVersion()));
            tv_version_os.setText(SystemUtilts.getVersionOs());
            tv_cpu_info.setText(SystemUtilts.getCpuName());
            tv_base_band_version.setText(SystemUtilts.getBaseband_Ver());
            tv_core_version.setText(SystemUtilts.getLinuxCore_Ver());
            tv_interior_version.setText(SystemUtilts.getInner_Ver());
            tv_screen_height.setText(String.valueOf(MyApplication.getScreenParameterHeight()));
            tv_screen_width.setText(String.valueOf(MyApplication.getScreenParameterWidth()));
            tv_phone_number.setText(SystemUtilts.getPhoneNumber(PhoneInfoActivity.this));
            tv_phone_ram.setText(SystemUtilts.getPhoneRAM(PhoneInfoActivity.this));
            tv_phone_all_ram.setText(SystemUtilts.getTotalMemory(PhoneInfoActivity.this)+"G");
            tv_imei_number.setText(SystemUtilts.getIMEINumber(PhoneInfoActivity.this));
            tv_phone_store.setText(SystemUtilts.getMemFree(PhoneInfoActivity.this));
            tv_phone_store_interior.setText(SystemUtilts.getSDSize(PhoneInfoActivity.this));
            tv_phone_store_exterior_all.setText("目前没有好的办法获取");//
            tv_phone_resolution_ratio.setText(getResolutionRatio());
            if(SystemUtilts.isRoot()){
                tv_phone_root.setText("true");
            }else {
                tv_phone_root.setText("false");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnShowRationale({Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE})
    void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog("获取手机相关信息需要申请相关权限，否则将获取不到信息", request);
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE})
    void onCameraDenied() {
        ToastUtils.showSingleTextToast(this,"你拒绝了权限，该功能不可用");
    }

    @OnNeverAskAgain({Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE})
    void onCameraNeverAskAgain() {
        ToastUtils.showSingleTextToast(this,"不再允许询问该权限，该功能不可用");
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


    private String getResolutionRatio() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        String s=dm.widthPixels+"*"+dm.heightPixels;
       MyLog.d("data",s);
        return s;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
