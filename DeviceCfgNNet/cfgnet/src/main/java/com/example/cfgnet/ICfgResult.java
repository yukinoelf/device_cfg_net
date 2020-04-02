package com.example.cfgnet;

import com.example.cfgnet.bean.ResultBean;

public interface ICfgResult {
    void onSuccess(ResultBean result);
    void onFail(int errorCode, String errorMsg);
}
