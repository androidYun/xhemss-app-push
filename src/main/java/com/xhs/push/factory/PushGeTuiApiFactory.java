package com.xhs.push.factory;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.xhs.push.application.IBaseApplication;

import java.util.HashMap;
import java.util.Map;

public class PushGeTuiApiFactory {
    public static PushGeTuiApiFactory getInstance() {
        return ViewHolderPushGeTuiApiFactory.PUSHGETUIAPIFACTORY;
    }

    final static class ViewHolderPushGeTuiApiFactory {
        static final PushGeTuiApiFactory PUSHGETUIAPIFACTORY = new PushGeTuiApiFactory();
    }

    private Map<String, ApiHelper> mGeTuiApiHelperHashMap = new HashMap<>();


    public <T> T getGeTuiApi(IBaseApplication iBaseApplication, Class<T> apiClass) {
        if (iBaseApplication == null || apiClass == null) {
            return null;
        }
        String geTuiApiHelperName = iBaseApplication.getAppName().concat(apiClass.getName());
        ApiHelper apiHelper = mGeTuiApiHelperHashMap.get(geTuiApiHelperName);
        if (apiHelper == null) {
            GtApiConfiguration apiConfiguration = new GtApiConfiguration();
            //填写应用配置
            apiConfiguration.setAppId(iBaseApplication.getGeTuiAppId());
            apiConfiguration.setAppKey(iBaseApplication.getGeTuiAppKey());
            apiConfiguration.setMasterSecret(iBaseApplication.getGeTuiMasterSecret());
            // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
            apiConfiguration.setDomain("https://restapi.getui.com/v2/");
            // 实例化ApiHelper对象，用于创建接口对象
            apiHelper = ApiHelper.build(apiConfiguration);
            mGeTuiApiHelperHashMap.put(geTuiApiHelperName, apiHelper);
        }
        return apiHelper.creatApi(apiClass);
    }
}
