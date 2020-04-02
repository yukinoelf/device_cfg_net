package com.example.cfgnet.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.cfgnet.bean.DeviceBean;
import com.example.cfgnet.bean.ResultBean;
import com.example.cfgnet.bean.WifiBean;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    private final static String TAG = JsonUtil.class.getSimpleName();
    private final static String KEY_WIFI = "wifi";
    private final static String KEY_PASSWORD = "password";
    private final static String KEY_DEVICE_SN = "deviceSn";

    public static String bean2JsonWifi(WifiBean wifiBean) {
        if (wifiBean == null || TextUtils.isEmpty(wifiBean.getWifi())) {
            Log.e(TAG, "bean2JsonWifi wifi null !");
            return null;
        }

        String password = TextUtils.isEmpty(wifiBean.getPassword()) ? "" : wifiBean.getPassword();

        JSONObject object = new JSONObject();
        try {
            object.put(KEY_WIFI, wifiBean.getWifi());
            object.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return object == null ? null : object.toString();
    }

    public static String bean2JsonDeviceSn(DeviceBean deviceBean) {
        if (deviceBean == null || TextUtils.isEmpty(deviceBean.getDeviceSn())) {
            Log.e(TAG, "bean2JsonDeviceSn deviceBean null !");
            return null;
        }

        JSONObject object = new JSONObject();
        try {
            object.put(KEY_DEVICE_SN, deviceBean.getDeviceSn());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return object == null ? null : object.toString();
    }
}
