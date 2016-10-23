package com.vincent.julie.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.vincent.julie.service.ProtectService;
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
import com.vincent.julie.view.SelectPicPopupWindow;
import com.vincent.julie.view.SlidingMenu;
import com.vincent.julie.view.ThreeDLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author Vincent QQ1032006226
 *         created at 2016/9/26 8:37
 */
@RuntimePermissions
public class MainActivity extends FragmentActivity implements BackHandledInterface {
    private LinearLayout llChat, llFriends, llContacts, llSettings, llSendMsg;
    private ImageView ivChat, ivFriends, ivContacts, ivSettings;
    private TextView tvChat, tvFriends, tvContacts, tvSettings;
    private CircleImageView circleImageView;
    private Button btnMenu;
    private FragmentSwitchTool tool;

    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "image";
    private int REQUESTCODE_TAKE = 11;
    private int REQUESTCODE_PICK = 12;
    private int REQUESTCODE_CUTTING = 13;

    private boolean isQuit = false;
    private BackHandledFragment mBackHandledFragment;


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
        if (llChat != null) {
            tool.changeTag(llChat);
        }

        //侧滑菜单相关
        menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);//选择照片
    }

    /**
     * 此方法配置顶部状态栏颜色
     */
    private void setStatusBar() {
        if (SystemUtilts.getAndroidSDKVersionInt() > 19 || SystemUtilts.getAndroidSDKVersionInt() == 19) {
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
     *
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
                if (!AppUtil.isServiceRunning(MainActivity.this, "com.vincent.julie.service.JulieService")) {
                    MyLog.d("主页", "JulieService is stop,start JulieService ...");
                    startService(new Intent(MainActivity.this, JulieService.class));
                }
                finish();
//                System.exit(0);
//                android.os.Process.killProcess(android.os.Process.myPid());//杀进程
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

        btnMenu = (Button) findViewById(R.id.button);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showSingleTextToast(MyApplication.getInstance(), "Click");
            }
        });
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showDefaultToast(MyApplication.getInstance(),"Click");
                menuWindow.showAtLocation(findViewById(R.id.id_menu),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        llSendMsg = (LinearLayout) findViewById(R.id.ll_send_msg);
        llSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendMsgActivity.class));
            }
        });
    }


    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandledFragment = selectedFragment;
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
//                    paiZhao();
                    MainActivityPermissionsDispatcher.paiZhaoWithCheck(MainActivity.this);
                    break;
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @NeedsPermission(Manifest.permission.CAMERA)
    void paiZhao() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForOpenCamera(PermissionRequest request) {
        showRationaleDialog("如果要自拍头像，需要授权打开照相机", request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void openCrmearDenied() {
        ToastUtils.showDefaultToast(getApplicationContext(), "您拒绝了授予权限，无法发送");
    }

    //拒绝权限不再弹出权限提示框
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void openCrmearNeverAskAgain() {
        ToastUtils.showDefaultToast(getApplicationContext(), "不再允许询问该权限，该功能不可用");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //弹出自定义权限提示框
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_PICK) {
            try {
                startPhotoZoom(data.getData());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUESTCODE_TAKE) {
            File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
            startPhotoZoom(Uri.fromFile(temp));
        } else if (requestCode == REQUESTCODE_CUTTING) {
            if (data != null) {
                setPicToView(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            addImg = photo;
            Drawable drawable = new BitmapDrawable(null, photo);
//            urlpath = FileUtil.saveFile(MainActivity.this, "temphead.jpg", photo);
            circleImageView.setImageDrawable(drawable);
        }
    }

}
