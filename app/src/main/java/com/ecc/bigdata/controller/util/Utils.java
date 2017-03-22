package com.ecc.bigdata.controller.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class Utils {
    public static final String URL = "http://58.16.67.76:9003/screen/goScreen";
    public static final String URL_PAD = "http://58.16.67.76:9003";
    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }
}
