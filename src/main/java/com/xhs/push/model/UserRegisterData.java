package com.xhs.push.model;

import lombok.Data;

@Data
public class UserRegisterData {
    String platformName = "";//手机类型类
    String registerId = "";
    String clientId = "";
    Boolean online = false;//是否在线
}
