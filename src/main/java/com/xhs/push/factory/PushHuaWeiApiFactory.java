package com.xhs.push.factory;

import com.huawei.push.messaging.HuaweiApp;
import com.huawei.push.messaging.HuaweiMessaging;
import com.huawei.push.util.InitAppUtils;
import com.xhs.push.application.IBaseApplication;

import java.util.HashMap;
import java.util.Map;

public class PushHuaWeiApiFactory {
    public static PushHuaWeiApiFactory getInstance() {
        return ViewHolderPushHuaWeiApiFactory.PushHuaWeiApiFactory;
    }

    final static class ViewHolderPushHuaWeiApiFactory {
        static final PushHuaWeiApiFactory PushHuaWeiApiFactory = new PushHuaWeiApiFactory();
    }

    private Map<String, HuaweiApp> mHuaWeiApiHelperHashMap = new HashMap<>();

    public HuaweiMessaging getHuaWeiMessage(IBaseApplication iBaseApplication) {
        if (iBaseApplication == null) {
            return null;
        }
        HuaweiApp huaweiApp = mHuaWeiApiHelperHashMap.get(iBaseApplication.getAppName());
        if (huaweiApp == null) {
            huaweiApp = InitAppUtils.initializeApp(iBaseApplication.getHuaWeiAppId(), iBaseApplication.getHuaWeiSecret());
            mHuaWeiApiHelperHashMap.put(iBaseApplication.getAppName(), huaweiApp);
        }
        return HuaweiMessaging.getInstance(huaweiApp);
    }
}
