package com.ecc.bigdata.tv.listener;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/10/26
 * BigDataScreen
 */

public abstract class LoadPagerListener {
    public abstract void onPageStarted();
    public abstract void onPageFinished();
    public abstract void onReceivedError();

    private volatile boolean loadOkFlag = true;

    protected boolean getLoadOkFlag() {
        return loadOkFlag;
    }

    public void setLoadOkFlag(boolean loadOkFlag) {
        this.loadOkFlag = loadOkFlag;
    }
}
