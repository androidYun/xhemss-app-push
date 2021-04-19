package com.xhs.push.channel;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;

abstract public class IBaseChannel {

    private IBaseApplication iBaseApplication;

    public IBaseChannel(IBaseApplication iBaseApplication) {
        this.iBaseApplication = iBaseApplication;
        initPushChanel(iBaseApplication);
    }

    public  abstract PushResult pushNotificationMessage(NPushMessage entity);

    public abstract PushResult pushTransmissionMessage(NPushMessage entity);

    public abstract PushResult pushNotificationMessageList(NPushMessage entity);

    public abstract PushResult pushTransmissionMessageList(NPushMessage entity);

    public  abstract PushResult initPushChanel(IBaseApplication iBaseApplication);

    public abstract String getPlatformName();
}
