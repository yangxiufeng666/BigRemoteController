package com.ecc.bigdata.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecc.bigdata.controller.bridge.JSserver;
import com.ecc.bigdata.controller.conf.XResourceClient;
import com.ecc.bigdata.controller.conf.XUIClient;
import com.ecc.bigdata.controller.listener.LoadPagerListener;
import com.orhanobut.logger.Logger;

import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkSettingsInternal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements JSserver.ClearCache{

    @BindView(R.id.xWalkView)
    XWalkView xWalkView;
    XWalkSettings mSettings;

    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;
    @BindView(R.id.btn_retry)
    TextView btnRetry;
    @BindView(R.id.loadFail)
    LinearLayout loadFailLayout;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    private boolean isError;
    private long exitTime;
    XLoadPagerListener loadPagerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initEnv();
        initWalkView();
    }

    private void initEnv() {
        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWalkView() {
        loadPagerListener = new XLoadPagerListener();
        xWalkView.setUIClient(new XUIClient(xWalkView, loadPagerListener));
        xWalkView.setResourceClient(new XResourceClient(xWalkView, loadPagerListener));

        xWalkView.setDrawingCacheEnabled(true);
        //获取setting
        mSettings = xWalkView.getSettings();
        //支持空间导航
        mSettings.setSupportSpatialNavigation(false);
        mSettings.setBuiltInZoomControls(false);
        mSettings.setSupportZoom(false);
        mSettings.setDomStorageEnabled(true);
        mSettings.setCacheMode(XWalkSettingsInternal.LOAD_DEFAULT);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setSupportSpatialNavigation(false);
        mSettings.setSupportMultipleWindows(false);
        mSettings.setSupportQuirksMode(false);
        mSettings.setAllowFileAccessFromFileURLs(true);
        mSettings.setAllowUniversalAccessFromFileURLs(true);
        mSettings.setAllowFileAccess(true);
        mSettings.setDatabaseEnabled(true);

        xWalkView.addJavascriptInterface(new JSserver(this,this), "NativeInterface");
        //load url
        if ("1".equals(getResources().getString(R.string.deviceType))){
            xWalkView.loadUrl(getResources().getString(R.string.domain_url));
        }else {
            xWalkView.loadUrl(getResources().getString(R.string.domain_url)+"/screen/goScreen");
        }
//        xWalkView.loadUrl("file:///android_asset/testJs.html");
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
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
        super.onDestroy();
    }

    @OnClick(R.id.btn_retry)
    public void onClick() {
        loadPagerListener.setLoadOkFlag(true);
        xWalkView.reload(XWalkView.RELOAD_IGNORE_CACHE);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
            return;
        }
        super.onBackPressed();
    }

    class XLoadPagerListener extends LoadPagerListener {
        @Override
        public void onPageStarted() {
            loadFailLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished() {
            Log.e("onPageFinished", "etLoadOkFlag()=" + getLoadOkFlag());
            if (!getLoadOkFlag()) {
                loadingLayout.setVisibility(View.GONE);
                loadFailLayout.setVisibility(View.VISIBLE);
            } else {
                loadFailLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedError() {
            loadingLayout.setVisibility(View.GONE);
            loadFailLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("resultCode="+resultCode);
        if (resultCode == JSserver.QR_SCAN_CODE){
            String result = data.getStringExtra("result");
            /**
             * {
             "token": "123456789",
             "imei": "234555"
             }
             */
            Logger.e("result="+result);
            //调用JS，把信息给页面
            xWalkView.loadUrl("javascript:show('"+result+"')");
        }
    }
    @Override
    public void clearCache() {
        xWalkView.clearCache(true);
    }
}
