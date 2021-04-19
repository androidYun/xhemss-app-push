package com.xhs.push.create;

import com.xhs.push.application.ApplicationFactory;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.ChannelFactory;
import com.xhs.push.channel.IBaseChannel;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;

public class YunPushSender<K extends IBaseChannel> {
    private Builder<K> mBuild;

    public YunPushSender(Builder<K> build) {
        this.mBuild = build;
    }

    public static class Builder<K extends IBaseChannel> {

        private IBaseApplication iBaseApplication;
        private Class<K> iBaseChannelClazz;
        public IBaseChannel iBaseChannel = null;

        /**
         * 设置不同Apk
         */
        public Builder<K> setApplication(IBaseApplication iBaseApplication) {
            this.iBaseApplication = iBaseApplication;
            return this;
        }

        public <T> Builder<K> setApplication(Class<T> claszz) {
            this.iBaseApplication = ApplicationFactory.getInstance().loadIBaseApplication(claszz);
            return this;
        }

        /**
         * 设置渠道
         */
        public Builder<K> setChannel(Class<K> claszz) {
            iBaseChannelClazz = claszz;
            return this;
        }

        public YunPushSender<K> build() throws Exception {
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

    /**
     * 发送通知消息
     */
    public PushResult sendNotificationMessage(NPushMessage pushMessage) throws Exception {
        if (mBuild == null || mBuild.iBaseChannel == null || pushMessage.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushMessage.getMRegisterList().size() == 1) {
            return mBuild.iBaseChannel.pushNotificationMessage(pushMessage);
        } else {
            return mBuild.iBaseChannel.pushNotificationMessageList(pushMessage);
        }
    }

    /**
     * 发送穿透消息
     */
    public PushResult sendTransmissionMessage(NPushMessage pushMessage) throws Exception {
        if (mBuild == null || mBuild.iBaseChannel == null || pushMessage.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushMessage.getMRegisterList().size() == 1) {
            return mBuild.iBaseChannel.pushTransmissionMessage(pushMessage);
        } else {
            return mBuild.iBaseChannel.pushTransmissionMessageList(pushMessage);
        }

    }
}
