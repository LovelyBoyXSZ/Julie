package com.vincent.julie.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

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
    /**
     *@param context
     * @param msg 
     *@author Vincent QQ:1032006226
     *create at 2016/9/28 22:29
     */
    public static void showDefaultToastCenter(Context context,String msg){
        toast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    /**
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * @param  context 上下文对象
     * @param msg 消息
     * @exception/throws [违例类型] [违例说明]
     * @see          [类、类#方法、类#成员]
     * 创建时间：2016/9/29 14:07
     * @author Vincent
     */
    public static void showSingleToastCenter(Context context,String msg){
        if(toast==null){
            toast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
