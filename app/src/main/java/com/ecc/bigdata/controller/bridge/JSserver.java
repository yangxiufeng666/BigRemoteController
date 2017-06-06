package com.ecc.bigdata.controller.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

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
    private ClearCache clearCache;

    public interface ClearCache{
        void clearCache();
    }

    public JSserver(Activity context) {
        this(context,null);
    }

    public JSserver(Activity context, ClearCache clearCache) {
        this.context = context;
        this.clearCache = clearCache;
    }

    @JavascriptInterface
    public String getImei(){
        return Utils.getDeviceUUID(context);
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
    @JavascriptInterface
    public void ClearCache(){
        if (clearCache != null){
            clearCache.clearCache();
        }
    }
}
