package com.ecc.bigdata.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceResponse;

import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;
import org.xwalk.core.internal.XWalkSettingsInternal;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.xWalkView)
    XWalkView xWalkView;
    XWalkSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initEnv();
        initWalkView();
        setWalkView();
    }
    private void initEnv(){
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setWalkView() {
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
            }

            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
            }

            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        xWalkView.setResourceClient(new XWalkResourceClient(xWalkView){
            @Override
            public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
                Log.e("WWalk","resource url = "+request.getUrl().toString());
                return super.shouldInterceptLoadRequest(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptLoadRequest(XWalkView view, String url) {
                return super.shouldInterceptLoadRequest(view, url);
            }

            @Override
            public void onLoadStarted(XWalkView view, String url) {
                super.onLoadStarted(view, url);
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
            }
        });
    }

    private void initWalkView() {
        xWalkView.loadUrl("http://58.16.67.76:9003/screen/goScreen");
        xWalkView.setDrawingCacheEnabled(true);
        //获取setting
        mSettings = xWalkView.getSettings();
        //支持空间导航
        mSettings.setSupportSpatialNavigation(false);
        mSettings.setBuiltInZoomControls(false);
        mSettings.setSupportZoom(false);
        mSettings.setDomStorageEnabled(true);
        mSettings.setJavaScriptEnabled(true);
        mSettings.setCacheMode(XWalkSettingsInternal.LOAD_NO_CACHE);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);

        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
    }

    @Override
    protected void onResume() {
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (xWalkView != null){
            xWalkView.onDestroy();
        }
        super.onDestroy();
    }
}
