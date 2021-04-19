package com.xhs.push.model;

import lombok.Data;

@Data
public class UserRegisterData {
    public UserRegisterData( String clientId, String registerId,String platformName, Boolean online) {
        this.platformName = platformName;
        this.registerId = registerId;
        this.clientId = clientId;
        this.online = online;
    }

    String platformName = "";//手机类型类
    String registerId = "";
    String clientId = "";
    Boolean online = false;//是否在线
}
