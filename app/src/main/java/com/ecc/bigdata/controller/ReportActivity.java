package com.ecc.bigdata.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ecc.bigdata.controller.entity.ResultBean;
import com.ecc.bigdata.controller.util.OKHttpUtil;
import com.ecc.bigdata.controller.util.PhoneUtil;
import com.ecc.bigdata.controller.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/3/22
 * BigDataController
 */

public class ReportActivity extends AppCompatActivity {
    @BindView(R.id.reportBtn)
    Button reportBtn;
    @BindView(R.id.phone)
    EditText phone;

    MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.reportBtn)
    public void onClick() {
        String phoneNum = phone.getText().toString();
        if (!PhoneUtil.isPhoneLegal(phoneNum)){
            Toast.makeText(ReportActivity.this,"电话号码不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        OkHttpClient okHttpClient = OKHttpUtil.getmInstance().getOKhttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(getResources().getString(R.string.domain_url)+"/report/device");
        builder.post(new FormBody.Builder()
                .add("imei", Utils.getIMEI(ReportActivity.this))
                .add("deviceType",getString(R.string.deviceType))
                .add("phone",phoneNum)
                .add("deviceName", Build.BRAND+"("+Build.MODEL+")")
                .build());
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dissmissWaitDialog();
                        showMsgDialog("上报设备信息失败，再试试看");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String content = response.body().string();
                Log.e("ReportActivity","content="+content);
                final ResultBean resultBean = gson.fromJson(content, ResultBean.class);
                if (resultBean==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dissmissWaitDialog();
                            showMsgDialog("上报设备信息失败，再试试看");
                        }
                    });
                }else {
                    if (resultBean.getCode()==100){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dissmissWaitDialog();
                                Toast.makeText(ReportActivity.this,resultBean.getMsg(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReportActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dissmissWaitDialog();
                                showMsgDialog(resultBean.getMsg());
                            }
                        });
                    }
                }
            }
        });
    }
    private void showMsgDialog(String content){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ReportActivity.this);
        builder.content(content);
        builder.title("温馨提示");
        builder.positiveText("知道了");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        MaterialDialog dialog = builder.build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void showWaitDialog(){
        if (dialog == null){
            MaterialDialog.Builder builder = new MaterialDialog.Builder(ReportActivity.this);
            builder.content("正在上报");
            builder.title("温馨提示");
            builder.progress(true,0);
            builder.positiveText("知道了");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.build();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }
    private void dissmissWaitDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }
}
