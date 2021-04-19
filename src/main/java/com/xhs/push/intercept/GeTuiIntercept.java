package com.xhs.push.intercept;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.ChannelFactory;
import com.xhs.push.channel.GeTuiChannelImpl;
import com.xhs.push.channel.IBaseChannel;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.UserRegisterData;
import com.xhs.push.utils.CopyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GeTuiIntercept extends IBaseIntercept {
    @Override
    IBaseChannel loadChannel(IBaseApplication iBaseApplication) {
        return ChannelFactory.getInstance().createChannel(GeTuiChannelImpl.class, iBaseApplication);
    }

    @Override
    public Boolean handleNoticeIntercept(NPushMessage entity, IBaseApplication iBaseApplication) throws Exception {
        if (iBaseApplication == null || entity.getMRegisterList().isEmpty()) {
            return false;
        }
        IBaseChannel iBaseChannel = loadChannel(iBaseApplication);
        if (iBaseChannel == null) {
            return false;
        }
        List<UserRegisterData> loadAllGeTuiList = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getClientId())).collect(Collectors.toList());
        if (!loadAllGeTuiList.isEmpty()) {//列表不为空进行渠道发送
            //拷贝一个对象
            NPushMessage deepCopy = CopyUtils.deepClone(entity);
            deepCopy.setMRegisterList(loadAllGeTuiList);
            if (loadAllGeTuiList.size() > 1) {
                iBaseChannel.pushNotificationMessageList(deepCopy);
            } else if (loadAllGeTuiList.size() == 1) {
                iBaseChannel.pushTransmissionMessage(deepCopy);
            }
        }
        if (nextBaseIntercept == null) return false;
        List<UserRegisterData>   loadOtherChannelList= entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).collect(Collectors.toList());
        if (!loadOtherChannelList.isEmpty()) {//不问空的情况下
            entity.setMRegisterList(loadOtherChannelList);
            return nextBaseIntercept.handleNoticeIntercept(
                    entity,
                    iBaseApplication
            );
        } else {
            return true;
        }
    }

    @Override
    public Boolean handleTransmissionIntercept(NPushMessage entity, IBaseApplication iBaseApplication) throws Exception {
        if (iBaseApplication == null || entity.getMRegisterList().isEmpty()) {
            return false;
        }
        IBaseChannel iBaseChannel = loadChannel(iBaseApplication);
        if (iBaseChannel == null) {
            return false;
        }
        List<UserRegisterData> loadAllGeTuiList = entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getClientId())).collect(Collectors.toList());
        if (!loadAllGeTuiList.isEmpty()) {//列表不为空进行渠道发送
            //拷贝一个对象
            NPushMessage deepCopy = CopyUtils.deepClone(entity);
            deepCopy.setMRegisterList(loadAllGeTuiList);
            if (loadAllGeTuiList.size() > 1) {
                iBaseChannel.pushNotificationMessageList(deepCopy);
            } else if (loadAllGeTuiList.size() == 1) {
                iBaseChannel.pushTransmissionMessage(deepCopy);
            }
        }
        if (nextBaseIntercept == null) return false;
        List<UserRegisterData>   loadOtherChannelList= entity.getMRegisterList().stream().filter(userRegisterData ->
                !StringUtils.isBlank(userRegisterData.getRegisterId())).collect(Collectors.toList());
        if (!loadOtherChannelList.isEmpty()) {//不问空的情况下
            entity.setMRegisterList(loadOtherChannelList);
            return nextBaseIntercept.handleTransmissionIntercept(
                    entity,
                    iBaseApplication
            );
        } else {
            return true;
        }
    }
}
