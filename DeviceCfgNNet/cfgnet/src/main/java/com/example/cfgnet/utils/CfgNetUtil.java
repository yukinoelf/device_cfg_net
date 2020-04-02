package com.example.cfgnet.utils;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.example.cfgnet.CfgStep;
import com.example.cfgnet.ErrorCode;
import com.example.cfgnet.ICfgResult;
import com.example.cfgnet.bean.DeviceBean;
import com.example.cfgnet.bean.ResultBean;
import com.example.cfgnet.bean.WifiBean;

import static com.example.cfgnet.CfgStep.ERROR;
import static com.example.cfgnet.CfgStep.FIND_DEVICE;
import static com.example.cfgnet.CfgStep.RUNNNING;
import static com.example.cfgnet.CfgStep.SEND_WIFI;
import static com.example.cfgnet.CfgStep.START;
import static com.example.cfgnet.CfgStep.STOP;
import static com.example.cfgnet.CfgStep.FINISH;

public class CfgNetUtil {
    private final static String TAG = CfgNetUtil.class.getSimpleName();
    private Context ctx;
    private ICfgResult iCfgResult;
    private Thread thread;
    private int scanInterval = 3000;
    private CfgStep step;
    private WifiBean wifiBean;
    private DeviceBean deviceBean;


    public void init(ICfgResult iCfgResult) {
        this.iCfgResult = iCfgResult;
    }

    public void setScanInterval(int scanInterval) {
        this.scanInterval = scanInterval;
    }

    public void startCfg(String wifi, String password) {
        step = START;
        if (thread != null && thread.isAlive()) {
            Log.i(TAG, "start: thread is alive");
        } else {
            wifiBean = new WifiBean();
            wifiBean.setWifi(wifi);
            wifiBean.setPassword(password);
            step = SEND_WIFI;

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (step != STOP && step != FINISH) {
                        cfgNetManager();
                        SystemClock.sleep(scanInterval);
                    }
                }
            });
            thread.start();
        }
    }

    public void stopCfg() {
        step = STOP;
    }

    private void cfgNetManager() {
        Log.i(TAG, "cfgNetManager step " + step);
        switch (step) {
            case SEND_WIFI:
                step = RUNNNING;
                sendWifi();
                break;
            case FIND_DEVICE:
                step = RUNNNING;
                findDevice();
                break;
            case RUNNNING:
                break;
            case ERROR:
            case FINISH:
                stopCfg();
                break;
        }
    }

    private void sendWifi() {
        String wifiJson = JsonUtil.bean2JsonWifi(wifiBean);
        if (wifiJson == null) {
            Log.e(TAG, "sendWifi wifiJson null !");
            onError(ErrorCode.FAILTURE_SEND_WIFI.getValue(), "wifiJson null");
            return;
        }

        String result = HttpUtil.doPost(HttpUtil.CHANNEL_DEVICE, wifiJson);
        Log.i(TAG, "sendWifi result " + result);

        if (HttpUtil.isSuccess(result) && !TextUtils.isEmpty(HttpUtil.getDeviceSn(result))) {
            deviceBean = new DeviceBean();
            deviceBean.setDeviceSn(HttpUtil.getDeviceSn(result));
            step = FIND_DEVICE;
        } else {
            onError(ErrorCode.FAILTURE_SEND_WIFI.getValue(), "net sendWifi fail");
        }
    }

    private void findDevice() {
        String device = JsonUtil.bean2JsonDeviceSn(deviceBean);
        if (device == null) {
            Log.e(TAG, "findDevice deviceSn null !");
            onError(ErrorCode.FAILTURE_FIND_DEVICE.getValue(), "deviceSn null");
            return;
        }

        String result = HttpUtil.doPost(HttpUtil.CHANNEL_SERVER, device);
        Log.i(TAG, "findDevice result " + result);

        if (HttpUtil.isSuccess(result) && HttpUtil.findDevice(result)) {
            onSuccess();
            step = FINISH;
        } else {
            onError(ErrorCode.FAILTURE_FIND_DEVICE.getValue(), "net findDevice fail");
        }
    }

    private void onError(int code, String msg) {
        step = ERROR;
        if (iCfgResult != null) {
            iCfgResult.onFail(code, msg);
        }
    }

    private void onSuccess() {
        step = FINISH;
        if (iCfgResult != null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setDeviceSn(deviceBean.getDeviceSn());
            iCfgResult.onSuccess(resultBean);
        }
    }
}
