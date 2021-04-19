package com.xhs.push.channel;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import org.json.simple.parser.ParseException;

import java.io.IOException;

abstract public class IBaseChannel {

    private IBaseApplication iBaseApplication;

    public IBaseChannel(IBaseApplication iBaseApplication) throws Exception {
        this.iBaseApplication = iBaseApplication;
        initPushChanel(iBaseApplication);
    }

    public  abstract PushResult pushNotificationMessage(NPushMessage entity) throws Exception;

    public abstract PushResult pushTransmissionMessage(NPushMessage entity) throws Exception;

    public abstract PushResult pushNotificationMessageList(NPushMessage entity) throws Exception;

    public abstract PushResult pushTransmissionMessageList(NPushMessage entity) throws Exception;

    public  abstract void  initPushChanel(IBaseApplication iBaseApplication) throws Exception;

    public abstract String getPlatformName();
}
