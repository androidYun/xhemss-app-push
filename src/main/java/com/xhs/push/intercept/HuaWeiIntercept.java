package com.xhs.push.intercept;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.ChannelFactory;
import com.xhs.push.channel.GeTuiChannelImpl;
import com.xhs.push.channel.HuaWeiChannelImpl;
import com.xhs.push.channel.IBaseChannel;

public class HuaWeiIntercept extends IBaseIntercept {
    @Override
    IBaseChannel loadChannel(IBaseApplication iBaseApplication) {
        return ChannelFactory.getInstance().createChannel(HuaWeiChannelImpl.class, iBaseApplication);
    }
}
