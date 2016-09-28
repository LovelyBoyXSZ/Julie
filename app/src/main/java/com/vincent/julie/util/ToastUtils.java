package com.vincent.julie.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Vincent on 2016/8/27.
 */

/**
  *Toast工具
  *@author Vincent QQ1032006226
  *created at 2016/9/26 10:22
  */
public class ToastUtils {
    private static Toast toast;

    /**
     * 如果当前Toast没有消失，继续点击事件不叠加
     * @param context
     * @param msg
     */
    public static void showSingleTextToast(Context context,String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 默认的Toast
     * @param context
     * @param msg
     */
    public static void showDefaultToast(Context context,String msg){
        Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

}
