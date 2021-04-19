package com.xhs.push.create;

import com.xhs.push.application.ApplicationFactory;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.ChannelFactory;
import com.xhs.push.channel.IBaseChannel;

public class YunPushSender<K extends IBaseChannel> {
    private Builder<K> build;

    public YunPushSender(Builder<K> build) {
        this.build = build;
    }

    class Builder<K extends IBaseChannel> {

        private IBaseApplication iBaseApplication;
        private Class<K> iBaseChannelClazz;
        IBaseChannel iBaseChannel = null;

        /**
         * 设置不同Apk
         */
        Builder<K> setApplication(IBaseApplication iBaseApplication) {
            this.iBaseApplication = iBaseApplication;
            return this;
        }

        <T> Builder<K> setApplication(Class<T> claszz) {
            this.iBaseApplication = ApplicationFactory.getInstance().loadIBaseApplication(claszz);
            return this;
        }

        /**
         * 设置渠道
         */
        Builder<K> setChannel(Class<K> claszz) {
            iBaseChannelClazz = claszz;
            return this;
        }

        YunPushSender<K> build() throws Exception {
            if (iBaseApplication == null) {
                throw new Exception("IBaseApplication is not null");
            }
            if (iBaseChannelClazz == null) {
                throw new Exception("iBaseChannel is not null");
            }
            if (iBaseApplication == null) {
                return null;
            }
            iBaseChannel = ChannelFactory.getInstance().createChannel(iBaseChannelClazz, iBaseApplication);
            return new YunPushSender(this);
        }
    }
}
