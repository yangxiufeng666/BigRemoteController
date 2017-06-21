package com.ecc.bigdata.controller;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.ecc.bigdata.controller.entity.ResultBean;
import com.ecc.bigdata.controller.util.OKHttpUtil;
import com.ecc.bigdata.controller.util.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };
    @BindView(R.id.lottieView)
    LottieAnimationView lottieView;

    CountDownLatch countDownLatch;

    volatile boolean isReported;
    volatile boolean isError;

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
        countDownLatch = new CountDownLatch(1);
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
        checkDeviceReport();
        lottieView.playAnimation();
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isError){
                    handler.postDelayed(runnable,300);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkDeviceReport() {
        Logger.e(Build.BRAND);
        Logger.e(Build.MODEL);
        OkHttpClient okHttpClient = OKHttpUtil.getmInstance().getOKhttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(getResources().getString(R.string.domain_url)+"/report/checkReportDevice");
        String imei = Utils.getDeviceUUID(SplashActivity.this);
        Logger.i("imei="+imei);
        builder.post(new FormBody.Builder()
                .add("imei", imei)
                .build());
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error();
                        isError = true;
                        countDownLatch.countDown();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ResultBean resultBean = gson.fromJson(response.body().string(), ResultBean.class);
                if (resultBean==null){
                    isReported = false;
                }else {
                    if (resultBean.getCode()==40000){//已经上报过了
                        isReported = true;
                    }else{
                        isReported = false;
                    }
                }
                countDownLatch.countDown();
            }
        });
    }
    private void error(){
        handler.removeCallbacks(runnable);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(SplashActivity.this);
        builder.content("网络连接失败，请检查您的网络");
        builder.title("温馨提示");
        builder.positiveText("退出应用");
        builder.negativeText("设置网络");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                SplashActivity.this.finish();
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                SplashActivity.this.finish();
                System.exit(0);
            }
        });
        MaterialDialog dialog = builder.build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent;
            if (isReported){
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }else{
                intent = new Intent(SplashActivity.this, ReportActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };
    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }
}
