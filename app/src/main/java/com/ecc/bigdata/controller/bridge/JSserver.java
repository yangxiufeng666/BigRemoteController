package com.ecc.bigdata.controller.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ecc.bigdata.controller.QRActivity;
import com.ecc.bigdata.controller.R;
import com.ecc.bigdata.controller.util.Utils;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class JSserver {
    public static final int QR_SCAN_CODE = 10001;
    private Activity context;

    public JSserver(Activity context) {
        this.context = context;
    }
    @JavascriptInterface
    public String getImei(){
        return Utils.getIMEI(context);
    }
    @JavascriptInterface
    public String getDeviceName(){
        return Build.BRAND+"("+Build.MODEL+")";
    }
    @JavascriptInterface
    public String getDeviceType(){
        return context.getString(R.string.deviceType);
    }
    @JavascriptInterface
    public void qrScan(){
        Intent intent = new Intent(context, QRActivity.class);
        context.startActivityForResult(intent,QR_SCAN_CODE);
    }
}
