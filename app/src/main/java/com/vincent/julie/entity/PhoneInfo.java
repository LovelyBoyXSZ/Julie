package com.vincent.julie.entity;

/**
 * Created by Vincent on 2016/10/23.
 */

public class PhoneInfo {

    private String name;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getString(){
        return name+info;
    }
}
