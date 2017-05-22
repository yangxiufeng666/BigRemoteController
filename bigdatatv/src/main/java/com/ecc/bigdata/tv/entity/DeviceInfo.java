package com.ecc.bigdata.tv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/4/7
 * BigDataController
 */

public class DeviceInfo implements Parcelable {
    private String token;
    private String imei;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.imei);
    }

    public DeviceInfo() {
    }

    protected DeviceInfo(Parcel in) {
        this.token = in.readString();
        this.imei = in.readString();
    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
