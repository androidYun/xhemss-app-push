package com.xhs.push;

import com.xhs.push.adapter.RepeatMessageAdapter;
import com.xhs.push.application.ApplicationFactory;
import com.xhs.push.application.DoctorApplicationImpl;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.ChannelFactory;
import com.xhs.push.channel.GeTuiChannelImpl;
import com.xhs.push.channel.XiaoMiChannelImpl;
import com.xhs.push.create.YunPushSender;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.UserRegisterData;
import com.xhs.push.utils.LoadIntentUtils;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class Main {
    @Test
    public void test() throws Exception {
        IBaseApplication loadIBaseApplication = ApplicationFactory.getInstance().loadIBaseApplication(DoctorApplicationImpl.class);
        List<UserRegisterData> mRegsiterList = new ArrayList();

        mRegsiterList.add(new UserRegisterData("fdba7c325b096aaf1c75e1d22f14f04e",
                "x1pc8OMQxHp+JMc0kP99dPmp+DjGFpxKlmGXsJDVoa7dXQWdldBq4b/emuJKTRA3",
                XiaoMiChannelImpl.PLATFORM_NAME,
                false));
        NPushMessage mNPushMessage = new NPushMessage();
        mNPushMessage.setGeTuiClickType("url");
        mNPushMessage.setUrl("https://docs.getui.com/getui/server/rest_v2/common_args/");
        String geTuiIntent = LoadIntentUtils.getInstance().getGeTuiIntent("{\"method\":\"newTask\",\"body\":{\"accidentName\":\"事故名称\",\"accidentCode\":\"任务编码\",\"accidentTaskCode\":\"子任务编码\",\"stationId\":\"分站ID\"}}",
                loadIBaseApplication.getPackage());
        System.out.println("打印测试" + geTuiIntent);
        mNPushMessage.setGeTuiIntent(geTuiIntent);
        mNPushMessage.setHuaWeiClickType(1);
        mNPushMessage.setHuaWeiIntent(LoadIntentUtils.getInstance().getHuaWeiIntent("{\"method\":\"newTask\",\"body\":{\"accidentName\":\"事故名称\",\"accidentCode\":\"任务编码\",\"accidentTaskCode\":\"子任务编码\",\"stationId\":\"分站ID\"}}"
        ));
        mNPushMessage.setTitle("内容");
        mNPushMessage.setBody("测试内容内容");
        mNPushMessage.setPayload("{\"method\":\"newEvent\",\"body\":{\"msgType\":\"new\",\"accidentCode\":\"任务编码\",\"accidentTaskCode\":\"子任务编码\",\"stationId\":\"分站ID\"}}");
        mNPushMessage.setMRegisterList(mRegsiterList);
//        /**
//         * 发送单个模式消息
//         */
//        YunPushSender pushSender = new YunPushSender.Builder<GeTuiChannelImpl>().setApplication(
//                loadIBaseApplication
//        ).setChannel(GeTuiChannelImpl.class).setPushMessage(mNPushMessage).build();
//        pushSender.sendTransmissionMessage();
//        /**
//         * 发送渠道类型
//         */
        RepeatMessageAdapter repeatMessageAdapter=new RepeatMessageAdapter(mNPushMessage,loadIBaseApplication);
        YunPushSender pushSender = new YunPushSender.Builder<GeTuiChannelImpl>().setAdapter(repeatMessageAdapter).build();
        pushSender.notificationAdapter();
    }
}
