package com.xhs.push.create;

import com.xhs.push.adapter.IPushMessageAdapter;
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
        public IPushMessageAdapter iPushMessageAdapter = null;
        public NPushMessage mPushMessage = null;

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

        public <T> Builder<K> setPushMessage(NPushMessage mPushMessage) {
            this.mPushMessage = mPushMessage;
            return this;
        }

        public <T> Builder<K> setAdapter(IPushMessageAdapter iPushMessageAdapter) {
            this.iPushMessageAdapter = iPushMessageAdapter;
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
            if (iPushMessageAdapter==null){
                if (iBaseApplication == null) {
                    throw new Exception("IBaseApplication is not null");
                }
                if (iBaseChannelClazz == null) {
                    throw new Exception("iBaseChannel is not null");
                }
                if (mPushMessage == null) {
                    throw new Exception("mPushMessage is not null");
                }
                iBaseChannel = ChannelFactory.getInstance().createChannel(iBaseChannelClazz, iBaseApplication);
            }
            return new YunPushSender(this);
        }
    }

    /**
     * 发送通知消息
     */
    public PushResult sendNotificationMessage() throws Exception {
        if (mBuild == null || mBuild.iBaseChannel == null || mBuild.mPushMessage.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (mBuild.mPushMessage.getMRegisterList().size() == 1) {
            return mBuild.iBaseChannel.pushNotificationMessage(mBuild.mPushMessage);
        } else {
            return mBuild.iBaseChannel.pushNotificationMessageList(mBuild.mPushMessage);
        }
    }

    /**
     * 发送穿透消息
     */
    public PushResult sendTransmissionMessage() throws Exception {
        if (mBuild.mPushMessage.getMRegisterList().size() == 1) {
            return mBuild.iBaseChannel.pushTransmissionMessage(mBuild.mPushMessage);
        } else {
            return mBuild.iBaseChannel.pushTransmissionMessageList(mBuild.mPushMessage);
        }
    }

    public void notificationAdapter() throws Exception {
        if (mBuild == null || mBuild.iPushMessageAdapter == null) {
            throw new Exception("adapter not is null");
        }
        mBuild.iPushMessageAdapter.notificationAdapter();
    }
}
