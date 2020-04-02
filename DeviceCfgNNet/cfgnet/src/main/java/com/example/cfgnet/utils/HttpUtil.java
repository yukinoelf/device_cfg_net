package com.example.cfgnet.utils;

import android.util.Log;

import com.example.cfgnet.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private final static String TAG = HttpUtil.class.getSimpleName();
    private final static int HTTP_RESULT_SUCCESS = 0;
    public final static int CHANNEL_DEVICE = 1;
    public final static int CHANNEL_SERVER = 2;

    public static String doPost(int channel, String jsonStr){
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;

        try{
            java.net.URL url = new URL(getHttpBase(channel));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                }
            }else{
                Log.e(TAG, "ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

    private static String getHttpBase(int channel) {
        String httpBase = "";
        switch (channel) {
            case CHANNEL_DEVICE:
                httpBase = BuildConfig.DEVICE_HTTP_BASE;
                break;
            case CHANNEL_SERVER:
                httpBase = BuildConfig.SERVER_HTTP_BASE;
                break;
        }
        return httpBase;
    }

    public static boolean isSuccess(String result) {
        int resultCode = -1;
        try {
            JSONObject jsonObject = new JSONObject(result);
            resultCode = Integer.parseInt(jsonObject.getString("result_code"));
            Log.i(TAG, "resultCode " + resultCode);
            if (resultCode == HTTP_RESULT_SUCCESS) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDeviceSn(String result) {
        String deviceSn = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            deviceSn = jsonObject.getJSONObject("data").getString("device_sn");
            Log.i(TAG, "getDeviceSn device sn " + deviceSn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceSn;
    }

    public static boolean findDevice(String result) {
        boolean isFindDevice = false;
        try {
            JSONObject jsonObject = new JSONObject(result);
            isFindDevice = jsonObject.getJSONObject("data").getBoolean("device_find");
            Log.i(TAG, "findDevice isFindDevice " + isFindDevice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isFindDevice;
    }
}
