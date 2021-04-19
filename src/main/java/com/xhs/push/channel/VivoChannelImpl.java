package com.xhs.push.channel;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;

public class VivoChannelImpl extends IBaseChannel {


    public VivoChannelImpl(IBaseApplication iBaseApplication) {
        super(iBaseApplication);
    }

    @Override
    public  PushResult initPushChanel(IBaseApplication iBaseApplication) {
        return null;
    }

    @Override
    public PushResult pushNotificationMessage(NPushMessage entity) {
        return null;
    }

    @Override
    public PushResult pushTransmissionMessage(NPushMessage entity) {
        return null;
    }

    @Override
    public PushResult pushNotificationMessageList(NPushMessage entity) {
        return null;
    }

    @Override
    public PushResult pushTransmissionMessageList(NPushMessage entity) {
        return null;
    }


    @Override
    public  String getPlatformName() {
        return PLATFORM_NAME;
    }

    final static String PLATFORM_NAME = "geTui";
}
