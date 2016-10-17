package com.vincent.julie.retrofit.model;

/**
 * 项目名称：Julie
 * 类名：com.vincent.julie.entity
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/17 16:43
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class WeatherEntity {
    public String resultcoe;//响应状态码  200-->获取数据成功
    public String reason;// successed  响应状态
    public String error_code;//错误码

    public class result{
        public class sk{
            private String temp;//28
            private String wind_direction;//东北风
            private String wind_strength;//1级
            private String humidity;//"69%
            private String time;//时间
        }
        public class today{
            private String wind;//威风
            private String week;//星期一
        }
    }
}
