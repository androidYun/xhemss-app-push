package com.xhs.push.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NPushMessage implements Serializable {
    List<UserRegisterData> mRegisterList;
    String title = "";
    String geTuiClickType = "none";//个推跳转方式  intent：打开应用内特定页面， url：打开网页地址，   payload：自定义消息内容启动应用，  payload_custom：自定义消息内容不启动应用， startapp：打开应用首页， none：纯通知，无后续动作
    int huaWeiClickType = 1;//华为跳转方式  1标识点击行为用户自定义; 2标识打开特定URL; 3标识打开本业务的APP; 4标识打开富媒体信息;
    int vivoClickType = 1;//1：打开APP首页 2：打开链接 3：自定义 4：打开app内指定页面
    int oppoClickType = 0;//点击动作类型0，启动应用；1，打开应用内页（activity的intent action）；2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;5;Intent scheme URL
    String geTuiIntent = "";
    String huaWeiIntent = "";
    String oppoIntent = "";
    String url = "";//跳转url
    String body = "";
    String payload = "";//附加消息 或者是
}
