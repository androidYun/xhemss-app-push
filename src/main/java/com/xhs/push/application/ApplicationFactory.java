package com.xhs.push.application;

import java.util.HashMap;
import java.util.Map;

public class ApplicationFactory {
    public static ApplicationFactory getInstance() {
        return ViewHolderApplicationFactory.APPLICATIONFACTORY;
    }

    final static class ViewHolderApplicationFactory {
        static final ApplicationFactory APPLICATIONFACTORY = new ApplicationFactory();
    }

    private Map<String, IBaseApplication> mIPushApplicationMap = new HashMap<>();

    /**
     * 先从map里面查询是否存在应用类，否则创建
     */
    public <T> IBaseApplication loadIBaseApplication(Class<T> clazz) {
        IBaseApplication iBaseApplication = mIPushApplicationMap.get(clazz.getName());
        if (iBaseApplication == null) {
            try {
                iBaseApplication = (IBaseApplication) clazz.newInstance();
                mIPushApplicationMap.put(clazz.getName(), iBaseApplication);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return iBaseApplication;
    }

}
