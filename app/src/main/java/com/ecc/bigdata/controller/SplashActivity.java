package com.ecc.bigdata.controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/2/14
 * BigDataController
 */

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "SplashActivity";

    private static final int PERMISSON_REQUESTCODE = 1000;
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };
    @BindView(R.id.welcomeTxt)
    TextView welcomeTxt;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        ButterKnife.bind(this);
        welcomeTxt.setText(getString(R.string.welcome_content));
        if (EasyPermissions.hasPermissions(this, needPermissions)) {
            Log.e(TAG, "已经获取权限");
            delayToMainActivity();
        } else {
            EasyPermissions.requestPermissions(this, "必要的权限", PERMISSON_REQUESTCODE, needPermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        ////把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsGranted 获取成功的权限" + perms);
        if (perms.containsAll(Arrays.asList(needPermissions))) {
            Log.e(TAG, "获取全部权限去主页面");
            delayToMainActivity();
        }
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle("提示")
                    .setRationale("当前应用缺少必要权限。请点击\"确认\"-\"权限\"-打开所需权限。")
                    .build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult requestCode = " + requestCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(this, needPermissions)) {
                Toast.makeText(this, "应用已经取得相应的权限，马上进入主界面", Toast.LENGTH_SHORT)
                        .show();
                delayToMainActivity();
            } else {
                Toast.makeText(this, "应用得不到相应的权限，即将退出", Toast.LENGTH_SHORT)
                        .show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }
    }

    private void delayToMainActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }
}
