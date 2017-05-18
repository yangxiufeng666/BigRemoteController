package com.ecc.bigdata.controller.conf;

import android.util.Log;

import com.ecc.bigdata.controller.R;
import com.ecc.bigdata.controller.listener.LoadPagerListener;
import com.ecc.bigdata.controller.util.Utils;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class XResourceClient extends XWalkResourceClient {
    private LoadPagerListener loadPagerListener;
    private List<String> staticResource;
    public XResourceClient(XWalkView view,LoadPagerListener loadPagerListener) {
        super(view);
        this.loadPagerListener = loadPagerListener;
        String[] resource = view.getResources().getStringArray(R.array.static_resource);
        staticResource = Arrays.asList(resource);
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        String url = request.getUrl().toString();
        Log.e("XResourceClient","resource url = "+url);
        int lastIndexOf = url.lastIndexOf("/");
        String steadContent = url.substring(lastIndexOf+1,url.length());
        Log.e("XResourceClient","steadContent = "+steadContent);
        if (staticResource.contains(steadContent)){
            try {
                XWalkWebResourceResponse response = createXWalkWebResourceResponse("text/javascript","UTF-8",view.getResources().getAssets().open("echarts/"+steadContent));
                Log.e("XResourceClient","重新生成");
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.shouldInterceptLoadRequest(view, request);
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        super.onLoadStarted(view, url);
        Log.e("XResourceClient","onLoadStarted url = "+url);
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        super.onLoadFinished(view, url);
        Log.e("XResourceClient","onLoadFinished url = "+url);
    }

    @Override
    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
        Log.e("XResourceClient","onReceivedLoadError = "+description+",,,,"+failingUrl+",");
//        super.onReceivedLoadError(view, errorCode, description, failingUrl);
        if (loadPagerListener != null){
            loadPagerListener.setLoadOkFlag(false);
            loadPagerListener.onReceivedError();
        }
    }


}
