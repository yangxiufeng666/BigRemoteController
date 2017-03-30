package com.ecc.bigdata.controller.bridge;

import android.content.Context;

import com.ecc.bigdata.controller.util.Utils;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class JSserver {
    private Context context;

    public JSserver(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public String getImei(){
        return Utils.getIMEI(context);
    }
}
