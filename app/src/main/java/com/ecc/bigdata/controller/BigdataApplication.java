package com.ecc.bigdata.controller;

import android.app.Application;

import com.ecc.bigdata.controller.util.DeviceUuidFactory;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

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
    }
    private void initLogger(){
        LogLevel logLevel;
        logLevel = LogLevel.FULL;
        Logger.init("Bigdata")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .logLevel(logLevel) ;       // default LogLevel.FULL
    }
}
