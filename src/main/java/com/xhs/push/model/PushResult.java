package com.xhs.push.model;

public class PushResult {
    /* 0成功  1失败 2用户不在线
     * */
    private int code;
    private String msg;
    /*
     * 注册id(个推是cid)
     * */
    private String registerId;

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

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }


    public static PushResult ok() {
        PushResult pushResult = new PushResult();
        pushResult.msg = "成功";
        pushResult.code = 0;
        return pushResult;
    }

    public static PushResult fail(String message) {//失败
        PushResult pushResult = new PushResult();
        pushResult.code = 1;
        return pushResult;
    }

    public static PushResult fail() {//失败
        PushResult pushResult = new PushResult();
        pushResult.code = 1;
        return pushResult;
    }

    public static PushResult offline() {//失败
        PushResult pushResult = new PushResult();
        pushResult.code = 2;
        return pushResult;
    }
}
