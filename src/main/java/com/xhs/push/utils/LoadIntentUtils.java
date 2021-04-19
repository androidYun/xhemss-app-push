package com.xhs.push.utils;

import org.apache.commons.lang3.StringUtils;

public class LoadIntentUtils {
    public static LoadIntentUtils getInstance() {
        return ViewHolderLoadIntentUtils.LOADINTENTUTILS;
    }

    final static class ViewHolderLoadIntentUtils {
        static final LoadIntentUtils LOADINTENTUTILS = new LoadIntentUtils();
    }

    String getGeTui(String payload, String packageName) throws Exception {
        if (StringUtils.isBlank(payload)) {
            throw new Exception("payload not support null");
        }
        if (StringUtils.isBlank(packageName)) {
            throw new Exception("packageName not support null");
        }
        return String.format("intent:#Intent;launchFlags=0x4000000;package={0};component={0}/com.push.core.link.GeTuiDeepLinkReceiveActivity;S.payload={1};end",
                packageName, new String(payload.getBytes(), "UTF-8"));
    }


    String getHuaWeiIntent(String payload) throws Exception {
        if (StringUtils.isBlank(payload)) {
            throw new Exception("payload not support null");
        }
        return String.format("intent://com.xhs.push/deepLink?#Intent;scheme=push;launchFlags=0x4000000;S.payload=%s;end", new String(payload.getBytes(), "UTF-8"));
    }

    String getOppoIntent() {
        return "com.push.core.link.OppoDeepLinkReceiveActivity";
    }
}
