package com.xhs.push.channel;

import com.vivo.push.sdk.notofication.Message;
import com.vivo.push.sdk.notofication.TargetMessage;
import com.vivo.push.sdk.server.Sender;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class VivoChannelImpl extends IBaseChannel {


    public VivoChannelImpl(IBaseApplication iBaseApplication) throws Exception {
        super(iBaseApplication);
    }

    private Sender mSender = null;

    /**
     * 初始化消息通知
     */
    public void initPushChanel(IBaseApplication iBaseApplication) throws Exception {
        mSender = new Sender(iBaseApplication.getGeTuiMasterSecret()); //注册登录开发平台网站获取到的appSecret
        mSender.initPool(20, 10);//设置连接池参数，可选项
        val result = mSender.getToken(iBaseApplication.getVivoAppId(), iBaseApplication.getVivoAppSecret()); //注册登录开发平台网站获取到的appId和appKey
        mSender.setAuthToken(result.getAuthToken());
    }
    @Override
    public PushResult pushNotificationMessage(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (StringUtils.isBlank(entity.getMRegisterList().get(0).getRegisterId())) {
            return PushResult.fail();
        }
        Message singleMessage = new Message.Builder() //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .regId(entity.getMRegisterList().get(0).getRegisterId())
                .notifyType(4)
                .title(entity.getTitle())
                .content(entity.getBody())
                .timeToLive(1000)
                .skipType(entity.getVivoClickType())
                .skipContent(entity.getUrl())//vivoClickType=2
                .networkType(-1)//-1：方式不限1：仅在wifi下发送
                .requestId(UUID.randomUUID().toString())
                .pushMode(0) //0正式 1 测试
                .classification(1)
                .build();
        val resultMessage = mSender.sendSingle(singleMessage);
        if (resultMessage.getResult() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushTransmissionMessage(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (StringUtils.isBlank(entity.getMRegisterList().get(0).getRegisterId())) {
            return PushResult.fail();
        }
        Message singleMessage = new Message.Builder() //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .regId(entity.getMRegisterList().get(0).getRegisterId())
                .notifyType(4)
                .title(entity.getTitle())
                .content(entity.getBody())
                .timeToLive(1000)
                .skipType(entity.getVivoClickType())
                .skipContent(entity.getUrl())//vivoClickType=2
                .networkType(-1)//-1：方式不限1：仅在wifi下发送
                .requestId(UUID.randomUUID().toString())
                .pushMode(0) //0正式 1 测试
                .classification(1)
                .build();
        val resultMessage = mSender.sendSingle(singleMessage);
        if (resultMessage.getResult() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushNotificationMessageList(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        Set<String> set = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toSet());
        if (set.isEmpty()) {//列表为空 那么返回错误
            return PushResult.fail();
        }
        val saveListPayload = saveListPayload(entity);
        if (StringUtils.isBlank(saveListPayload)) {
            return PushResult.fail();
        }
        TargetMessage singleMessage = new TargetMessage.Builder() //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .regIds(set)
                .taskId(saveListPayload)
                .requestId(UUID.randomUUID().toString())
                .pushMode(0) //0正式 1 测试
                .build();
        val resultMessage = mSender.sendToList(singleMessage);//发送保存群推消息请求
        if (resultMessage.getResult() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }

    }
    @Override
    public PushResult pushTransmissionMessageList(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        Set<String> set = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toSet());
        if (set.isEmpty()) {//列表为空 那么返回错误
            return PushResult.fail();
        }
        val saveListPayload = saveListPayload(entity);
        if (StringUtils.isBlank(saveListPayload)) {
            return PushResult.fail();
        }
        TargetMessage singleMessage = new TargetMessage.Builder() //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .regIds(set)
                .taskId(saveListPayload)
                .requestId(UUID.randomUUID().toString())
                .pushMode(0) //0正式 1 测试
                .build();
        val resultMessage = mSender.sendToList(singleMessage);//发送保存群推消息请求
        if (resultMessage.getResult() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }

    String saveListPayload(NPushMessage entity) throws Exception {
        val singleMessage = new Message.Builder() //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .notifyType(4)
                .title(entity.getTitle())
                .content(entity.getBody())
                .timeToLive(1000)
                .skipType(entity.getVivoClickType())
                .skipContent(entity.getUrl())
                .networkType(-1)
                .requestId(UUID.randomUUID().toString())
                .pushMode(0) //0正式 1 测试
                .classification(1)
                .build();
        val result = mSender.saveListPayLoad(singleMessage); //发送保存群推消息请求
        if (result.getResult() == 0) {
            return result.getTaskId();
        } else {
            return "";
        }
    }

    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    final static String PLATFORM_NAME = "vivo";
}
