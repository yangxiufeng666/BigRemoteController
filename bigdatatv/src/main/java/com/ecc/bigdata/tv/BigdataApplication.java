package com.ecc.bigdata.tv;

import android.app.Application;

import com.ecc.bigdata.tv.util.DeviceUuidFactory;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/4/7
 * BigDataController
 */

public class BigdataApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeviceUuidFactory.buildUuidFactory(this);
        initLogger();
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
        CrashReport.initCrashReport(getApplicationContext(), "1eb624abfc", true);
    }
    private void initLogger(){
        LogLevel logLevel;
        logLevel = LogLevel.FULL;
        Logger.init("Bigdata")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .logLevel(logLevel) ;       // default LogLevel.FULL
    }
}
