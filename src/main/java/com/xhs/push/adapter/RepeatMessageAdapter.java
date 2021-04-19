package com.xhs.push.adapter;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.intercept.GeTuiIntercept;
import com.xhs.push.intercept.IBaseIntercept;
import com.xhs.push.intercept.LoadPlatformHeadIntercept;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import com.xhs.push.model.UserRegisterData;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RepeatMessageAdapter extends IPushMessageAdapter {
    private IBaseIntercept mGeTuiIntercept;

    public RepeatMessageAdapter(NPushMessage nNPushMessage, IBaseApplication iBaseApplication) {
        super(nNPushMessage, iBaseApplication);
        mGeTuiIntercept=new GeTuiIntercept();
    }

    @Override
    void convertMessage(NPushMessage nNPushMessage, IBaseApplication iBaseApplication) throws Exception {
        if (nNPushMessage == null || iBaseApplication == null) {
            return;
        }
        if (nNPushMessage.getMRegisterList().isEmpty()) {
            return;
        }
        /**
         * 先用个推发送消息
         */
        if (!nNPushMessage.getMRegisterList().isEmpty()) {
            mGeTuiIntercept.handleTransmissionIntercept(nNPushMessage, iBaseApplication);
        }
        /**
         * 然后在发通知消息
         */
        List<UserRegisterData> platformList = nNPushMessage.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).collect(Collectors.toList());
        IBaseIntercept loadPlatformHeadIntercept = LoadPlatformHeadIntercept.getInstance().loadPlatformHeadIntercept();
        if (platformList.isEmpty()||loadPlatformHeadIntercept==null) {
            PushResult.fail();
        } else {
            nNPushMessage.setMRegisterList(platformList);
            loadPlatformHeadIntercept.handleTransmissionIntercept(nNPushMessage, iBaseApplication);
        }
    }
}
