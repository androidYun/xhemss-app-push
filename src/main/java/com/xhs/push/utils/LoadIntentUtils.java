package com.xhs.push.utils;

import org.apache.commons.lang3.StringUtils;

public class LoadIntentUtils {
    public static LoadIntentUtils getInstance() {
        return ViewHolderLoadIntentUtils.LOADINTENTUTILS;
    }

    final static class ViewHolderLoadIntentUtils {
        static final LoadIntentUtils LOADINTENTUTILS = new LoadIntentUtils();
    }

    public String getGeTuiIntent(String payload, String packageName) throws Exception {
        if (StringUtils.isBlank(payload)) {
            throw new Exception("payload not support null");
        }
        if (StringUtils.isBlank(packageName)) {
            throw new Exception("packageName not support null");
        }
        return String.format("intent:#Intent;launchFlags=0x4000000;package=%s};component=%s/com.push.core.link.GeTuiDeepLinkReceiveActivity;S.payload=%s;end",
                packageName, packageName,EncodeUtil.convertStringToUTF8(payload));
    }


    public String getHuaWeiIntent(String payload) throws Exception {
        if (StringUtils.isBlank(payload)) {
            throw new Exception("payload not support null");
        }
        return String.format("intent://com.xhs.push/deepLink?#Intent;scheme=push;launchFlags=0x4000000;S.payload=%s;end",EncodeUtil.convertStringToUTF8(payload));
    }

    String getOppoIntent() {
        return "com.push.core.link.OppoDeepLinkReceiveActivity";
    }
}
