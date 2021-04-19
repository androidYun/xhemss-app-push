package com.xhs.push.adapter;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;

abstract public class IPushMessageAdapter {
    private NPushMessage nNPushMessage;
    private IBaseApplication iBaseApplication;
    public IPushMessageAdapter(NPushMessage nNPushMessage, IBaseApplication iBaseApplication) {
        this.nNPushMessage = nNPushMessage;
        this.iBaseApplication = iBaseApplication;
    }

    abstract void convertMessage(NPushMessage nNPushMessage, IBaseApplication iBaseApplication);


    void notificationAdapter() {
        convertMessage(nNPushMessage, iBaseApplication);
    }
}
