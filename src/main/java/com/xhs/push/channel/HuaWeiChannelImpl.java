package com.xhs.push.channel;

import com.huawei.push.android.*;
import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.AndroidConfig;
import com.huawei.push.message.Message;
import com.huawei.push.message.Notification;
import com.huawei.push.messaging.HuaweiApp;
import com.huawei.push.model.Importance;
import com.huawei.push.model.Urgency;
import com.huawei.push.model.Visibility;
import com.huawei.push.reponse.SendResponse;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.factory.PushHuaWeiApiFactory;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import com.xhs.push.model.UserRegisterData;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class HuaWeiChannelImpl extends IBaseChannel {

    private IBaseApplication iBaseApplication;

    public HuaWeiChannelImpl(IBaseApplication iBaseApplication) throws Exception {
        super(iBaseApplication);
        this.iBaseApplication = iBaseApplication;
    }

    /**
     * 初始化消息通知
     */
    public void initPushChanel(IBaseApplication iBaseApplication) {
    }
    @Override
    public PushResult pushNotificationMessage(NPushMessage entity) throws HuaweiMesssagingException {
        List<String> list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        Notification notification = new Notification().builder().setTitle(entity.getTitle())
                .setBody(entity.getBody())
                .build();
        LightSettings lightSettings = LightSettings.builder().setColor(Color.builder().setAlpha(0f).setRed(0f).setBlue(1f).setGreen(1f).build())
                .setLightOnDuration("3.5")
                .setLightOffDuration("5S")
                .build();
        AndroidNotification androidNotification = AndroidNotification.builder().setIcon("/raw/ic_launcher2")
                .setColor("#ff0000")
                .setSound("/raw/shake")
                .setDefaultSound(true)
                .setTag("tagBoom")
                .setClickAction(ClickAction.builder()
                        .setType(entity.getHuaWeiClickType())//1标识点击行为用户自定义; 2标识打开特定URL; 3标识打开本业务的APP; 4标识打开富媒体信息;
                        .setIntent(entity.getHuaWeiIntent())
                        .setUrl(entity.getUrl()).build())
                .setBodyLocKey("M.String.body")
                .addBodyLocArgs("boy").addBodyLocArgs("dog")
                .setTitleLocKey("M.String.title")
                .addTitleLocArgs("Girl").addTitleLocArgs("Cat")
                .setChannelId("Your Channel ID")
                .setNotifySummary("some summary")
                .setStyle(1)
                .setBigTitle(entity.getTitle())
                .setBigBody(entity.getBody())
                .setAutoClear(1000 * 60 * 10)//显示多长时间  10分钟自动消息
                .setNotifyId(486)
                .setGroup(iBaseApplication.getAppName())//一个app的消息进行分组
                .setImportance(Importance.LOW.getValue())
                .setLightSettings(lightSettings)
                .setBadge(BadgeNotification.builder().setAddNum(1).setBadgeClass("Classic").build())
                .setVisibility(Visibility.PUBLIC.getValue())
                .setForegroundShow(true).build();

        AndroidConfig androidConfig = AndroidConfig.builder().setCollapseKey(-1)
                .setUrgency(Urgency.HIGH.getValue())
                .setTtl("10000s")
                .setBiTag("the_sample_bi_tag_for_receipt_service")
                .setNotification(androidNotification)
                .build();
        Message message = Message.builder().setNotification(notification)
                .setAndroidConfig(androidConfig)
                .addAllToken(list)
                .build();
        SendResponse response = PushHuaWeiApiFactory.getInstance().getHuaWeiMessage(iBaseApplication).sendMessage(message);
        if ("80000000".equals(response.getCode())) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushTransmissionMessage(NPushMessage entity) throws HuaweiMesssagingException {
        List<String> list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(UserRegisterData::getRegisterId).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        AndroidConfig androidConfig = AndroidConfig.builder().setCollapseKey(-1)
                .setUrgency(Urgency.HIGH.getValue())
                .setTtl("10000s")//设置消息缓存时间
                .setBiTag("the_sample_bi_tag_for_receipt_service")
                .build();
        Message message = Message.builder()
                .setData("{'k1':'v1', 'k2':'v2'}")
                .setAndroidConfig(androidConfig)
                .addAllToken(list)
                .build();
        SendResponse response = PushHuaWeiApiFactory.getInstance().getHuaWeiMessage(iBaseApplication).sendMessage(message);
        if ("80000000".equals(response.getCode())) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushNotificationMessageList(NPushMessage entity) throws HuaweiMesssagingException {
        return pushNotificationMessage(entity);
    }
    @Override
    public PushResult pushTransmissionMessageList(NPushMessage entity) throws HuaweiMesssagingException {
        return pushTransmissionMessage(entity);
    }

    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    final static String PLATFORM_NAME = "geTui";
}
