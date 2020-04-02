package com.example.cfgnet.bean;

public class ResultBean {
    private String deviceSn;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    public String toString() {
        return "ResultBean"
                + " deviceSn " + deviceSn;
    }
}
