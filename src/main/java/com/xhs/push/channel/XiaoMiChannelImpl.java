package com.xhs.push.channel;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class XiaoMiChannelImpl extends IBaseChannel {

    IBaseApplication iBaseApplication = null;

    public XiaoMiChannelImpl(IBaseApplication iBaseApplication) {
        super(iBaseApplication);
        this.iBaseApplication = iBaseApplication;
    }

    private Sender mSender = null;

    /**
     * 初始化消息通知
     */
    @Override
    public void initPushChanel(IBaseApplication iBaseApplication) {
        Constants.useOfficial();
        mSender = new Sender(iBaseApplication.getXiaoMiSecret());
    }


    public PushResult pushNotificationMessage(NPushMessage entity) throws IOException, ParseException {
        List list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        Message message = new Message.Builder()
                .title(entity.getTitle())
                .description(entity.getBody()).payload(entity.getPayload())
                .notifyType(0)//1：使用默认提示音提示2：使用默认震动提示4：使用默认led灯光提示-1（系统默认值）：以上三种效果都有0：以上三种效果都无，即静默推送。
                .passThrough(0)//1 传透  0 通知
                .restrictedPackageName(iBaseApplication.getPackage())
                .timeToLive(1000 * 60 * 10)
                .build();
        Result result = mSender.send(message, list, 2);
        if (result.getErrorCode() == ErrorCode.Success) {
            return PushResult.ok();
        } else {
            return PushResult.fail(result.getReason());
        }
    }

    public PushResult pushTransmissionMessage(NPushMessage entity) throws IOException, ParseException {
        List list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        Message message = new Message.Builder()
                .title(entity.getTitle())
                .description(entity.getBody()).payload(entity.getPayload())
                .notifyType(0)//1：使用默认提示音提示2：使用默认震动提示4：使用默认led灯光提示-1（系统默认值）：以上三种效果都有0：以上三种效果都无，即静默推送。
                .passThrough(1)//1 传透  0 通知
                .restrictedPackageName(iBaseApplication.getPackage())
                .timeToLive(1000 * 60 * 10)
                .build();
        Result result = mSender.send(message, list, 2);
        if (result.getErrorCode() == ErrorCode.Success) {
            return PushResult.ok();
        } else {
            return PushResult.fail(result.getReason());
        }
    }

    public PushResult

    pushNotificationMessageList(NPushMessage entity) throws IOException, ParseException {
        return pushNotificationMessage(entity);
    }

    public PushResult pushTransmissionMessageList(NPushMessage entity) throws IOException, ParseException {
        return pushNotificationMessage(entity);
    }


    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    final static String PLATFORM_NAME = "xiaoMi";
}
