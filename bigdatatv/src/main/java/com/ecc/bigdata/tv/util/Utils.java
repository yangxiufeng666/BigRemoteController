package com.ecc.bigdata.tv.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class Utils {



    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        String imei = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"获取IMEI失败="+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        if ("012345678912345".equals(imei)){
            return null;
        }
        return imei;
    }
    public static String getMac(Context context){
        WifiManager wm = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String  mac = wm.getConnectionInfo().getMacAddress();
        return mac;
    }
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" +
                // 主板
                Build.BOARD.length() % 10 +
                // android系统定制商
                Build.BRAND.length() % 10 +
                // cpu指令集
                Build.CPU_ABI.length() % 10 +
                // 设备参数
                Build.DEVICE.length() % 10 +
                // 显示屏参数
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                // 修订版本列表
                Build.ID.length() % 10 +
                // 硬件制造商
                Build.MANUFACTURER.length() % 10 +
                // 版本
                Build.MODEL.length() % 10 +
                // 手机制造商
                Build.PRODUCT.length() % 10 +
                // 描述build的标签
                Build.TAGS.length() % 10 +
                // builder类型
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位

        //A hardware serial number, if available. Alphanumeric only, case-insensitive.
        String serial = Build.SERIAL;
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }
}
