package com.xhs.push.create;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.IBaseChannel;

public class YunPushSender<K extends IBaseChannel> {

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

        fun<T :IBaseApplication> setApplication(Class<T> claszz):Builder<K>

        {
            this.iBaseApplication = ApplicationFactory.loadIBaseApplication(claszz)
            return this
        }

        /**
         * 设置渠道
         */
        fun setChannel(claszz:Class<K>):Builder<K>

        {
            iBaseChannelClazz = claszz
            return this
        }

        fun build():_root_ide_package_.com.xhs.push.create.YunPushSender<K>?

        {
            if (iBaseApplication == null) {
                throw Exception("IBaseApplication is not null")
                return null
            }
            if (iBaseChannelClazz == null) {
                throw Exception("iBaseChannel is not null")
                return null
            }
            if (iBaseApplication == null) {
                return null
            }
            iBaseChannel = ChannelFactory.createChannel(iBaseChannelClazz !!, iBaseApplication)
            return _root_ide_package_.com.xhs.push.create.YunPushSender(this)
        }
    }
}
