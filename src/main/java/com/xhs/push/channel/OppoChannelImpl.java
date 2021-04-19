package com.xhs.push.channel;

import com.oppo.push.server.Notification;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OppoChannelImpl extends IBaseChannel {


    private Sender mSender;

    public OppoChannelImpl(IBaseApplication iBaseApplication) throws Exception {
        super(iBaseApplication);
    }


    @Override
    public void initPushChanel(IBaseApplication iBaseApplication) throws Exception {
        mSender = new Sender(iBaseApplication.getOppoAppKey(), iBaseApplication.getOppoAppSecret()); //注册登录开发平台网站获取到的appSecret
    }

    /**
     * 初始化消息通知
     */

    @Override
    public PushResult pushNotificationMessage(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (StringUtils.isBlank(entity.getMRegisterList().get(0).getRegisterId())) {
            return PushResult.fail();
        }
        val notification = getNotification(entity); //创建通知栏消息体
        Target target = Target.build(entity.getMRegisterList().get(0).getRegisterId()); //创建发送对象
        val result = mSender.unicastNotification(notification, target); //发送单推消息
        if (result.getReturnCode().getCode() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }

    public PushResult

    pushTransmissionMessage(NPushMessage entity) throws Exception {
        if (entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (StringUtils.isBlank(entity.getMRegisterList().get(0).getRegisterId())) {
            return PushResult.fail();
        }
        val notification = getNotification(entity); //创建通知栏消息体
        Target target = Target.build(entity.getMRegisterList().get(0).getRegisterId()); //创建发送对象
        val result = mSender.unicastNotification(notification, target); //发送单推消息
        if (result.getReturnCode().getCode() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushNotificationMessageList(NPushMessage entity) throws Exception {
        List<String> list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        val batch = new HashMap<Target, Notification>(); // batch最大为1000
        for (String token : list) {
            batch.put(Target.build(token), getNotification(entity));
        }
        val result = mSender.unicastBatchNotification(batch); //发送单推消息
        if (result.getReturnCode().getCode() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }
    @Override
    public PushResult pushTransmissionMessageList(NPushMessage entity) throws Exception {
        List<String> list = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).map(userRegisterData ->
                userRegisterData.getRegisterId()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return PushResult.fail();
        }
        val batch = new HashMap<Target, Notification>(); // batch最大为1000
        for (String token : list) {
            batch.put(Target.build(token), getNotification(entity));
        }
        val result = mSender.unicastBatchNotification(batch); //发送单推消息
        if (result.getReturnCode().getCode() == 0) {
            return PushResult.ok();
        } else {
            return PushResult.fail();
        }
    }

    private Notification getNotification(NPushMessage entity) {
        Notification notification = new Notification();
        /**
         * 以下参数必填项
         */
        notification.setTitle(entity.getTitle());
        notification.setContent(entity.getBody());
        //以下参数非必填项， 通知栏样式 1. 标准样式  2. 长文本样式  3. 大图样式 【非必填，默认1-标准样式】
        notification.setStyle(1);
        // App开发者自定义消息Id，OPPO推送平台根据此ID做去重处理，对于广播推送相同appMessageId只会保存一次，对于单推相同appMessageId只会推送一次
        notification.setAppMessageId(UUID.randomUUID().toString());
        // 应用接收消息到达回执的回调URL，字数限制200以内，中英文均以一个计算
        notification.setCallBackUrl(entity.getUrl());
        // App开发者自定义回执参数，字数限制50以内，中英文均以一个计算
        notification.setCallBackParameter(entity.getPayload());
        // 点击动作类型0，启动应用；1，打开应用内页（activity的intent action）；2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;5,Intent scheme URL
        notification.setClickActionType(entity.getOppoClickType());
        // 应用内页地址【click_action_type为1或4时必填，长度500】
        notification.setClickActionActivity(entity.getOppoIntent());
        // 网页地址【click_action_type为2必填，长度500】
        if (entity.getOppoClickType() == 2)
            notification.setClickActionUrl(entity.getUrl());
        // 动作参数，打开应用内页或网页时传递给应用或网页【JSON格式，非必填】，字符数不能超过4K，示例：{"key1":"value1","key2":"value2"}
        notification.setActionParameters(entity.getPayload());
        // 展示类型 (0, “即时”),(1, “定时”)
        notification.setShowTimeType(0);
        // 定时展示开始时间（根据time_zone转换成当地时间），时间的毫秒数
        notification.setShowStartTime(System.currentTimeMillis() + 1000 * 60 * 3);
        // 定时展示结束时间（根据time_zone转换成当地时间），时间的毫秒数
        notification.setShowEndTime(System.currentTimeMillis() + 1000 * 60 * 5);
        // 是否进离线消息,【非必填，默认为True】
        notification.setOffLine(true);
        // 离线消息的存活时间(time_to_live) (单位：秒), 【off_line值为true时，必填，最长3天】
        notification.setOffLineTtl(24 * 3600);
        // 时区，默认值：（GMT+08:00）北京，香港，新加坡
        notification.setTimeZone("GMT+08:00");
        // 0：不限联网方式, 1：仅wifi推送
        notification.setNetworkType(0);
        return notification;
    }

    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    public final static String PLATFORM_NAME = "Vivo";
}
