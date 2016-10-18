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

    private String resultcode;//响应状态码  200-->获取数据成功
    private String reason;// successed  响应状态
    private String error_code;//错误码
    private result result;

    public WeatherEntity.result getResult() {
        return result;
    }

    public void setResult(WeatherEntity.result result) {
        this.result = result;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public static class result{
        private today today;
        private sk sk;

        public WeatherEntity.result.today getToday() {
            return today;
        }

        public void setToday(WeatherEntity.result.today today) {
            this.today = today;
        }

        public WeatherEntity.result.sk getSk() {
            return sk;
        }

        public void setSk(WeatherEntity.result.sk sk) {
            this.sk = sk;
        }

        public class sk{
            private String temp;//28
            private String wind_direction;//东北风
            private String wind_strength;//1级
            private String humidity;//"69%
            public String time;//时间
        }
        public class today{
            private String wind;//威风
            private String week;//星期一

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }
        }
    }

}
