package com.vincent.julie.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.util.FileUtil;
import com.vincent.julie.util.ToastUtils;
import com.vincent.julie.view.SelectPicPopupWindow;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 二维码扫描与生成相关类
 *
 * @author Vincent QQ1032006226
 *         created at 2016/9/27 15:36
 */
@RuntimePermissions
public class QrScodeUtilsActivity extends BaseActivity{

    private static final String IMAGE_FILE_NAME = "image";
    @BindView(R.id.iv_qr_code_scan)
    ImageView ivQrCodeScan;
    @BindView(R.id.tv_qr_code_create)
    TextView tvQrCodeCreate;
    @BindView(R.id.iv_qr_code_add_img)
    ImageView ivQrCodeAddImg;
    @BindView(R.id.et_two_code_intent)
    EditText etTwoCodeIntent;
    @BindView(R.id.rl_open_input)
    RelativeLayout rlOpenInput;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    private int REQUESTCODE_TAKE = 11;
    private int REQUESTCODE_PICK = 12;
    private int REQUESTCODE_CUTTING = 13;

    private String urlpath = "";
    private Bitmap addImg = null;

    private SelectPicPopupWindow menuWindow;
    private ClipboardManager copy;

    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_qr_code);
        setTitleText("二维码扫描与生成");
        copy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        menuWindow = new SelectPicPopupWindow(this, itemsOnClick);//选择照片
        //这里设置ivQrCode的长按事件会报空指针
    }

    //分享单张图片
    public void shareSingleImage(String path) {
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
//        Uri imageUri = Uri.fromFile(new File(imagePath));
        Uri imageUri = Uri.fromFile(new File(path));
        MyLog.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 保存推按到手机
     * 返回保存的路径，次路径携带了图片名字
     *
     * @param context
     * @param bmp
     * @return
     */
    public static String saveImageToGallery(Context context, Bitmap bmp) {

        String path = "";

        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            path = file.getAbsolutePath();
            MyLog.d("path", path);
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        return path;
    }

    @Override
    protected void setListener() {

    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ivQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MyLog.d("long click ivQrCode", "长按分享会把此图片保存在手机里面");
                if (ivQrCode != null) {
                    Bitmap bm = ((BitmapDrawable) ivQrCode.getDrawable()).getBitmap();
                    if (bm == null) {
                        return false;
                    } else {
                        shareSingleImage(saveImageToGallery(QrScodeUtilsActivity.this, bm));
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.iv_qr_code_scan, R.id.tv_qr_code_create, R.id.iv_qr_code_add_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_qr_code_scan:
                QrScodeUtilsActivityPermissionsDispatcher.startScanWithCheck(QrScodeUtilsActivity.this);
                break;
            case R.id.tv_qr_code_create:
                String content = etTwoCodeIntent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showDefaultToast(MyApplication.getInstance().getApplicationContext(), "还没有输入内容，请输入");
                    MyLog.d("two code ", "error,未输入");
                    return;
                }
                generateQrCode(content);
                break;
            case R.id.iv_qr_code_add_img:
                MyLog.d("Add img", "click");
                menuWindow.showAtLocation(ivQrCode,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startScan() {
        startActivityForResult(new Intent(QrScodeUtilsActivity.this, CaptureActivity.class), 0);//启动扫描
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开照相机的权限,存储权限用于保存并分享二维码", request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        ToastUtils.showSingleTextToast(getApplicationContext(), "你拒绝了权限，无法扫描");
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraNeverAskAgain() {
        ToastUtils.showSingleTextToast(getApplicationContext(), "不再允许询问该权限，该功能不可用");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        QrScodeUtilsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_PICK) {
            try {
                startPhotoZoom(data.getData());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUESTCODE_TAKE) {
            File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
            startPhotoZoom(Uri.fromFile(temp));
        } else if (requestCode == 0) {//二维码扫描
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                String utf8Result = "";
                try {
                    utf8Result = URLDecoder.decode(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(utf8Result)) {
                    return;
                }
                if (TextUtils.isEmpty(utf8Result)) {
                    ToastUtils.showDefaultToast(MyApplication.getInstance(), "没有数据");
                    MyLog.d("two code scan", "result is null");
                } else {
                    if (Patterns.WEB_URL.matcher(utf8Result).matches()) {//判断结果是不是一个网址，如果是，就打开
                       /* Intent intent = new Intent(getActivity(), CommonOpenUrlActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title", "二维码扫描结果");
                        intent.putExtra("url", utf8Result);
                        startActivity(intent);*/
//                        copy.setText(utf8Result);//此方法过时用下面的方法替换
                        ClipData clipData = ClipData
                                .newPlainText("url text label", utf8Result);
                        copy.setPrimaryClip(clipData);
                        MyLog.d("url text label",utf8Result);
                        ToastUtils.showSingleTextToast(QrScodeUtilsActivity.this, "结果\"" + utf8Result + "\"已复制到截切板");
                    } else {
//                        copy.setText(utf8Result);
                        ClipData clipData = ClipData
                                .newPlainText("text label", utf8Result);
                        copy.setPrimaryClip(clipData);
                        MyLog.d("text label",utf8Result);
                        ToastUtils.showDefaultToast(MyApplication.getInstance(), "结果\"" + utf8Result + "\"已复制到截切板");
                    }
                }
            }
        } else if (requestCode == REQUESTCODE_CUTTING) {
            if (data != null) {
                setPicToView(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 生成二维码的方法
     *
     * @param content
     */
    private void generateQrCode(String content) {
        if (null != addImg) {//有图片
            Bitmap bitmap = EncodingUtils.createQRCode(content, 500, 500, addImg);
            ivQrCode.setImageBitmap(bitmap);
        } else {//无图片
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap bitmap = EncodingUtils.createQRCode(content, 500, 500, null);
            ivQrCode.setImageBitmap(bitmap);
        }
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
            addImg = photo;
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(this, "temphead.jpg", photo);
//			clvHead.setImageDrawable(drawable);
        }
    }

}
