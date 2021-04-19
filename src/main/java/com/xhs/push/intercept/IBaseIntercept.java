package com.xhs.push.intercept;

import com.xhs.push.application.IBaseApplication;
import com.xhs.push.channel.IBaseChannel;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.UserRegisterData;
import com.xhs.push.utils.CopyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class IBaseIntercept {

    private IBaseIntercept nextBaseIntercept;

    abstract IBaseChannel loadChannel(IBaseApplication iBaseApplication);

    Boolean handleNoticeIntercept(NPushMessage entity, IBaseApplication iBaseApplication) throws IOException, ClassNotFoundException {
        if (iBaseApplication == null || entity.getMRegisterList().isEmpty()) {
            return false;
        }
        IBaseChannel iBaseChannel = loadChannel(iBaseApplication);
        if (iBaseChannel == null) {
            return false;
        }
        List<UserRegisterData> loadCurrentChannelList = loadCurrentChannelList(entity.getMRegisterList(), iBaseChannel);
        if (!loadCurrentChannelList.isEmpty()) {//列表不为空进行渠道发送
            //拷贝一个对象
            NPushMessage deepCopy = CopyUtils.deepClone(entity);
            deepCopy.setMRegisterList(loadCurrentChannelList);
            if (loadCurrentChannelList.size() > 1) {
                iBaseChannel.pushNotificationMessageList(deepCopy);
            } else if (loadCurrentChannelList.size() == 1) {
                iBaseChannel.pushTransmissionMessage(deepCopy);
            }
        }
        if (nextBaseIntercept == null) return false;
        List<UserRegisterData> loadOtherChannelList = loadOtherChannelList(entity.getMRegisterList(), iBaseChannel);
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

    Boolean handleTransmissionIntercept(NPushMessage entity, IBaseApplication iBaseApplication) throws IOException, ClassNotFoundException {
        if (iBaseApplication == null || entity.getMRegisterList().isEmpty()) {
            return false;
        }
        IBaseChannel iBaseChannel = loadChannel(iBaseApplication);
        if (iBaseChannel == null) {
            return false;
        }
        List<UserRegisterData> loadCurrentChannelList = loadCurrentChannelList(entity.getMRegisterList(), iBaseChannel);
        if (!loadCurrentChannelList.isEmpty()) {//列表不为空进行渠道发送
            //拷贝一个对象
            NPushMessage deepCopy = CopyUtils.deepClone(entity);
            deepCopy.setMRegisterList(loadCurrentChannelList);
            if (loadCurrentChannelList.size() > 1) {
                iBaseChannel.pushTransmissionMessageList(deepCopy);
            } else if (loadCurrentChannelList.size() == 1) {
                iBaseChannel.pushTransmissionMessage(deepCopy);
            }
        }
        if (nextBaseIntercept == null) {
            return false;
        }
        List<UserRegisterData> loadOtherChannelList = loadOtherChannelList(entity.getMRegisterList(), iBaseChannel);
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

    /**
     * 加载当前渠道的列表
     */
    private List<UserRegisterData> loadCurrentChannelList(List<UserRegisterData> mRegisterList, IBaseChannel iBaseChannel) {
        if (mRegisterList.isEmpty()) {
            return new ArrayList<>();
        }
        return mRegisterList.stream().filter(userRegisterData -> iBaseChannel.getPlatformName().equals(userRegisterData.getPlatformName())).collect(Collectors.toList());
    }

    /**
     * 加载不同渠道的列表
     */
    private List<UserRegisterData> loadOtherChannelList(List<UserRegisterData> mRegisterList, IBaseChannel iBaseChannel) {
        if (mRegisterList.isEmpty()) {
            return new ArrayList<>();
        }
        return mRegisterList.stream().filter(userRegisterData -> !iBaseChannel.getPlatformName().equals(userRegisterData.getPlatformName())).collect(Collectors.toList());
    }

}
