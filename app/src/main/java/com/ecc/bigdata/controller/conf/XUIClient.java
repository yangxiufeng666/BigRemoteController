package com.ecc.bigdata.controller.conf;

import android.util.Log;

import com.ecc.bigdata.controller.listener.LoadPagerListener;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class XUIClient extends XWalkUIClient{
    private LoadPagerListener loadPagerListener;

    public XUIClient(XWalkView view,LoadPagerListener loadPagerListener) {
        super(view);
        this.loadPagerListener = loadPagerListener;
    }

    @Override
    public void onPageLoadStarted(XWalkView view, String url) {
        super.onPageLoadStarted(view, url);
        Log.e("XUIClient","onPageLoadStarted = "+url);
        if (loadPagerListener != null){
            loadPagerListener.onPageStarted();
        }
    }

    @Override
    public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
        super.onPageLoadStopped(view, url, status);
        Log.e("XUIClient","onPageLoadStopped = "+url + ",status = " + status );
        if (loadPagerListener != null){
            switch (status.ordinal()){
                case 0:
                    loadPagerListener.onPageFinished();
                    break;
                case 1:
                    loadPagerListener.onReceivedError();
                    break;
                case 2:
                    loadPagerListener.onReceivedError();
                    break;
            }
        }
        //强制不能返回，清除历史记录
        view.getNavigationHistory().clear();

    }


}
