package com.ecc.bigdata.controller.conf;

import android.util.Log;

import com.ecc.bigdata.controller.listener.LoadPagerListener;
import com.ecc.bigdata.controller.util.Utils;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class XResourceClient extends XWalkResourceClient {
    private LoadPagerListener loadPagerListener;
    public XResourceClient(XWalkView view,LoadPagerListener loadPagerListener) {
        super(view);
        this.loadPagerListener = loadPagerListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        Log.e("WWalk","resource url = "+request.getUrl().toString());
        return super.shouldInterceptLoadRequest(view, request);
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        super.onLoadStarted(view, url);
        Log.e("WWalk","onLoadStarted url = "+url);
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        super.onLoadFinished(view, url);
        Log.e("WWalk","onLoadFinished url = "+url);
    }

    @Override
    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
        Log.e("XResourceClient","onReceivedLoadError = "+description+",,,,"+failingUrl+",");
        super.onReceivedLoadError(view, errorCode, description, failingUrl);
        if (loadPagerListener != null && failingUrl.equals(Utils.URL)){
            loadPagerListener.setLoadOkFlag(false);
            loadPagerListener.onReceivedError();
        }
    }

}
