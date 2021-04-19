package com.xhs.push.channel;

import com.xhs.push.application.ApplicationFactory;
import com.xhs.push.application.IBaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ChannelFactory {
    public static ChannelFactory getInstance() {
        return ViewHolderChannelFactory.CHANNELFACTORY;
    }

    final static class ViewHolderChannelFactory {
        static final ChannelFactory CHANNELFACTORY = new ChannelFactory();
    }

    private Map<String, IBaseChannel> mPushChannelHashMap = new HashMap<>();

    /**
     * 先从map里面查询是否存在应用类，否则创建
     */
    public <T> IBaseChannel createChannel(
            Class<T> clazz,
            IBaseApplication iBaseApplication
    ) {
        String channelName = clazz.getName().concat("-").concat(iBaseApplication.getAppName());
        IBaseChannel iBaseChannel = mPushChannelHashMap.get(channelName);
        if (iBaseChannel == null) {
            try {
                iBaseChannel = (IBaseChannel) clazz.getConstructor(
                        IBaseApplication.class
                ).newInstance(iBaseApplication);  //获取有参构造
                mPushChannelHashMap.put(channelName, iBaseChannel);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return iBaseChannel;
    }

    /**
     * 先从map里面查询是否存在应用类，否则创建
     */
    public <T> IBaseChannel loadChannel(
            Class<T> clazz,
            Class<IBaseApplication> iBaseApplicationClaszz
    ) {
        IBaseApplication iBaseApplication = ApplicationFactory.getInstance().loadIBaseApplication(iBaseApplicationClaszz);
        if (iBaseApplication == null) {
            return null;
        }
        String channelName = clazz.getName().concat("-").concat(iBaseApplication.getAppName());
        IBaseChannel iBaseChannel = mPushChannelHashMap.get(channelName);
        if (iBaseChannel == null) {
            try {
                iBaseChannel = (IBaseChannel) clazz.getConstructor(
                        IBaseApplication.class
                ).newInstance(iBaseApplication);  //获取有参构造
                mPushChannelHashMap.put(channelName, iBaseChannel);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return iBaseChannel;
    }
}
