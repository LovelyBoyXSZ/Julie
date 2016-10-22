package com.vincent.julie.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vincent.julie.R;
import com.vincent.julie.app.BaseActivity;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.logs.MyLog;
import com.vincent.julie.netutil.NetUtil;
import com.vincent.julie.util.SystemUtilts;
import com.vincent.julie.util.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by Vincent on 2016/10/22.
 */

public class InternetActivity extends BaseActivity {

    private ProgressBar pbLine;
    private WebView webView;
    private static final String TAG=InternetActivity.class.getSimpleName();

    private final static int FILECHOOSER_RESULTCODE = 1;
    private android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;
    private Uri imageUri;

    private String phoneManufacturer;//手机厂商



    @Override
    protected void titleBarRightClick() {

    }

    @Override
    protected void findViewById() {
        setContentView(R.layout.act_internet);
        hideTitle();
        webView=(WebView)findViewById(R.id.webview);
        pbLine=(ProgressBar)findViewById(R.id.pb_common_line);
        phoneManufacturer = SystemUtilts.getPhoneManufacturer();
        MyLog.d("phoneMdel", phoneManufacturer);
        showWebView();
    }

    @Override
    protected void setListener() {

    }


    @SuppressLint("JavascriptInterface")
    private void showWebView() {
        try {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDefaultTextEncodingName("utf-8");
//            webView.addJavascriptInterface(new JsOperation(activity, context), "jsObj");//为了和js交互
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.loadUrl("https://www.baidu.com");
            webView.getSettings().setUseWideViewPort(true);//让网页 大小自适应屏幕
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        return false;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                @Override
                public void onReceivedError(WebView webView, int i, String s, String s1) {
                    super.onReceivedError(webView, i, s, s1);
                    if (NetUtil.hasNetwork(InternetActivity.this)) {
                        ToastUtils.showSingleTextToast(MyApplication.getInstance(),"数据错误");
                        MyLog.d("InternetActivity","数据错误");
//                        webView.loadUrl("file:///android_asset/nonetwork/data_error.html");
                    } else {
                        ToastUtils.showSingleTextToast(MyApplication.getInstance(),"没有网络了");
                        MyLog.d("InternetActivity","无网络");
                    }

                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView webView, int newProgress) {
                    if(newProgress==100){
                        //隐藏进度条
                        pbLine.setVisibility(View.GONE);
                    }else {
                        pbLine.setVisibility(View.VISIBLE);
                    }
                    super.onProgressChanged(webView, newProgress);
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    MyLog.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    InternetActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                }
                public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                    MyLog.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    InternetActivity.this.startActivityForResult(
                            Intent.createChooser(i, "File Browser"),
                            FILECHOOSER_RESULTCODE);
                }
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                    MyLog.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    InternetActivity.this.startActivityForResult( Intent.createChooser( i, "File Browser" ), InternetActivity.FILECHOOSER_RESULTCODE );
                }

                // For Android 5.0+
                public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                    mUploadCallbackAboveL = filePathCallback;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    InternetActivity.this.startActivityForResult(
                            Intent.createChooser(i, "File Browser"),
                            FILECHOOSER_RESULTCODE);
                    return true;
                }
            });

            /**
             * 屏蔽长按事件
             */
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setPositiveButton("离开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                        }
                    }).setNegativeButton("我再看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setMessage("是否离开当前页面？")
                    .show();
        }
        return false;
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.w("InternetActivity","onDestroy");
        webView.clearCache(true);
        webView.clearHistory();
        webView=null;

    }

    @Override
    public void onResume() {
        super.onResume();
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    public void onClick(View v) {

    }
}
