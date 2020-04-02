package com.example.liulei.devicecfgnnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cfgnet.ICfgResult;
import com.example.cfgnet.bean.ResultBean;
import com.example.cfgnet.utils.CfgNetUtil;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private CfgNetUtil cfgNetUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cfgNetUtil = new CfgNetUtil();
        cfgNetUtil.init(new ICfgResult() {
            @Override
            public void onSuccess(ResultBean result) {
                Log.i(TAG, "onSuccess " + result.toString());
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                Log.e(TAG, "onFail errorCode " + errorCode + " errorMsg " + errorMsg);
            }
        });

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfgNetUtil.startCfg("yuki", "12345678");
            }
        });

    }
}
