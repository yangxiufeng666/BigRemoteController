package com.ecc.bigdata.tv.entity;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2017/3/22
 * BigDataController
 */

public class ResultBean {
    private int code;
    private String msg;
    private int msgType;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
