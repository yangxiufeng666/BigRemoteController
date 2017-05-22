package com.ecc.bigdata.tv.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/3/21
 * BigDataController
 */

public class OKHttpUtil {
    private volatile static OKHttpUtil mInstance;
    private OkHttpClient okHttpClient;
    private OKHttpUtil() {
    }

    public static OKHttpUtil getmInstance() {
        if (mInstance == null){
            synchronized (OKHttpUtil.class){
                mInstance = new OKHttpUtil();
            }
        }
        return mInstance;
    }

    public OkHttpClient getOKhttpClient(){
        if (okHttpClient != null){
            return okHttpClient;
        }
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(20,TimeUnit.SECONDS);
        builder.readTimeout(20,TimeUnit.SECONDS);
        okHttpClient = builder.build();
        return okHttpClient;
    }
}
