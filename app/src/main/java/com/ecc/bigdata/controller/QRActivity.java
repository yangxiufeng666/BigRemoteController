package com.ecc.bigdata.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ecc.bigdata.controller.bridge.JSserver;
import com.ecc.bigdata.controller.entity.DeviceInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/4/7
 * BigDataController
 */

public class QRActivity extends AppCompatActivity implements QRCodeView.Delegate{

    private static final String TAG = "QRActivity";

    @BindView(R.id.zxingview)
    ZXingView mQRCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        mQRCodeView.setDelegate(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.stopCamera();
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        Logger.i("re="+result);
        vibrate();
        mQRCodeView.stopSpot();
        Gson gson = new Gson();
        try {
            DeviceInfo info = gson.fromJson(result, DeviceInfo.class);
            Intent intent = new Intent();
            intent.putExtra("result",result);
            setResult(JSserver.QR_SCAN_CODE,intent);
            finish();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            new MaterialDialog.Builder(QRActivity.this)
                    .title("Hi~")
                    .content("二维码信息不正确")
                    .positiveText("重新扫码")
                    .negativeText("返回")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mQRCodeView.startSpot();
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .show();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
        new MaterialDialog.Builder(QRActivity.this)
                .title("Hi~")
                .content("打开相机出错")
                .positiveText("返回")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
