package com.ecc.bigdata.tv.bridge;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;


import com.ecc.bigdata.tv.R;
import com.ecc.bigdata.tv.util.DeviceUuidFactory;
import com.ecc.bigdata.tv.util.Utils;

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
        String deviceUUID =Utils.getDeviceUUID(context);
        return deviceUUID;
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
    public float getXdpi(){
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float xdpi = metric.xdpi;
        Log.e("metric", "  DisplayMetrics, xdpi=" + xdpi);
        return xdpi;
    }
    @JavascriptInterface
    public float getYdpi(){
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float ydpi = metric.ydpi;
        Log.e("metric", "  DisplayMetrics,  ydpi=" + ydpi);
        return ydpi;
    }
}
