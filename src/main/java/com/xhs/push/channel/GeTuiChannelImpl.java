package com.xhs.push.channel;

import com.alibaba.fastjson.JSON;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.AudienceDTO;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.xhs.push.application.IBaseApplication;
import com.xhs.push.factory.PushGeTuiApiFactory;
import com.xhs.push.model.NPushMessage;
import com.xhs.push.model.PushResult;
import com.xhs.push.model.UserRegisterData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public class GeTuiChannelImpl extends IBaseChannel {
    IBaseApplication iBaseApplication = null;
    private PushApi pushApi = null;

    public GeTuiChannelImpl(IBaseApplication iBaseApplication) throws Exception {
        super(iBaseApplication);
        this.iBaseApplication = iBaseApplication;
    }

    @Override
    public void initPushChanel(IBaseApplication iBaseApplication) {
        pushApi = PushGeTuiApiFactory.getInstance().getGeTuiApi(iBaseApplication, PushApi.class);
    }

    @Override
    public PushResult pushNotificationMessage(NPushMessage entity) {
        if (entity == null || entity.getMRegisterList() == null || entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushApi==null){
            pushApi = PushGeTuiApiFactory.getInstance().getGeTuiApi(iBaseApplication, PushApi.class);
        }
        PushDTO<Audience> pushDTO = getAudiencePushDTO(entity);
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.addCid(entity.getMRegisterList().get(0).getClientId());
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) { // success
            return PushResult.ok();
        } else { // failed
            log.info("code=" + apiResult.getCode() + ",msg=" + JSON.toJSONString(apiResult));
            return PushResult.fail();
        }
    }

    @Override
    public PushResult pushNotificationMessageList(NPushMessage entity) {
        if (entity == null || entity.getMRegisterList() == null || entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushApi==null){
            pushApi = PushGeTuiApiFactory.getInstance().getGeTuiApi(iBaseApplication, PushApi.class);
        }
        PushDTO<Audience> pushDTO = getAudiencePushDTO(entity);
        String taskId = createMessage(pushDTO);
        if (StringUtils.isBlank(taskId)) {
            return PushResult.fail();
        }
        AudienceDTO audienceDTO = new AudienceDTO();
        Audience audience = new Audience();
        for (UserRegisterData userRegisterData : entity.getMRegisterList()) {
            if (StringUtils.isBlank(userRegisterData.getClientId())) {
                audience.addCid(userRegisterData.getClientId());
            }
        }
        audienceDTO.setAudience(audience);
        audienceDTO.setTaskid(taskId);
        audienceDTO.isAsync();
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushListByCid(audienceDTO);
        return getResult(apiResult);

    }
    @Override
    public PushResult pushTransmissionMessageList(NPushMessage entity) {
        if (entity == null || entity.getMRegisterList() == null || entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushApi==null){
            pushApi = PushGeTuiApiFactory.getInstance().getGeTuiApi(iBaseApplication, PushApi.class);
        }
        PushDTO pushDTO = getTransmissionAudiencePushDTO(entity);
        String taskId = createTransmissionMessage(pushDTO);
        if (StringUtils.isBlank(taskId)) {
            return PushResult.fail();
        }
        AudienceDTO audienceDTO = new AudienceDTO();
        Audience audience = new Audience();
        for (UserRegisterData userRegisterData : entity.getMRegisterList()) {
            if (StringUtils.isBlank(userRegisterData.getClientId())) {
                audience.addCid(userRegisterData.getClientId());
            }
        }
        audienceDTO.setAudience(audience);
        audienceDTO.setTaskid(taskId);
        audienceDTO.isAsync();
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushListByCid(audienceDTO);
        return getResult(apiResult);
    }
    @Override
    public PushResult pushTransmissionMessage(NPushMessage entity) {
        if (entity == null || entity.getMRegisterList() == null || entity.getMRegisterList().isEmpty()) {
            return PushResult.fail();
        }
        if (pushApi==null){
            pushApi = PushGeTuiApiFactory.getInstance().getGeTuiApi(iBaseApplication, PushApi.class);
        }
        PushDTO pushDTO = getTransmissionAudiencePushDTO(entity);
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.addCid(entity.getMRegisterList().get(0).getClientId());
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) { // success
            return PushResult.ok();
        } else { // failed
            log.info("code=" + apiResult.getCode() + ",msg=" + JSON.toJSONString(apiResult));
            return PushResult.fail();
        }
    }


    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    /**
     * 群发 获取任务的taskId
     */
    private String createMessage(PushDTO<Audience> pushDTO) {
        ApiResult<TaskIdDTO> apiResult = pushApi.createMsg(pushDTO);
        if (apiResult != null && apiResult.isSuccess()) {
            TaskIdDTO data = apiResult.getData();
            if (data == null) {
                return "";
            }
            return data.getTaskId();
        }
        return "";
    }

    private PushDTO<Audience> getAudiencePushDTO(NPushMessage entity) {
        PushDTO<Audience> pushDTO = new PushDTO<>();
        String groupName = String.valueOf(System.currentTimeMillis());
        String requestId = String.valueOf(System.currentTimeMillis());
        pushDTO.setGroupName(groupName);
        pushDTO.setRequestId(requestId);
        GTNotification gtNotification = new GTNotification();
        gtNotification.setLogo(iBaseApplication.getLogo());
        gtNotification.setBody(entity.getBody());
        gtNotification.setTitle(entity.getTitle());
        gtNotification.setClickType(entity.getGeTuiClickType());
        gtNotification.setPayload(entity.getPayload());
        gtNotification.setUrl("url".equals(entity.getGeTuiClickType()) ? entity.getUrl() : null);
        gtNotification.setIntent("intent".equals(entity.getGeTuiClickType()) ? entity.getGeTuiIntent() : null);
        PushMessage pushMessage = new PushMessage();
        pushMessage.setNotification(gtNotification);
        pushDTO.setPushMessage(pushMessage);
        return pushDTO;
    }

    private String createTransmissionMessage(PushDTO<Audience> pushDTO) {
        ApiResult apiResult = pushApi.createMsg(pushDTO);
        if (apiResult != null && apiResult.isSuccess() && apiResult.getData() != null) {
            return String.valueOf(apiResult.getData());
        }
        return null;
    }

    /**
     * 获取传透消息PushDto
     */
    private PushDTO<Audience> getTransmissionAudiencePushDTO(NPushMessage entity) {
        PushDTO<Audience> pushDTO = new PushDTO<>();
        String groupName = String.valueOf(System.currentTimeMillis());
        pushDTO.setGroupName(groupName);
        // 设置推送参数
        pushDTO.setRequestId(String.valueOf(System.currentTimeMillis()));
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTransmission(entity.getPayload());
        pushDTO.setPushMessage(pushMessage);
        return pushDTO;
    }

    /**
     * 返回结果封装
     */
    private PushResult getResult(ApiResult<Map<String, Map<String, String>>> apiResult) {
        if (apiResult != null && apiResult.isSuccess()) {
            return PushResult.ok();
        }
        log.info("code=" + apiResult.getCode() + ",msg=" + JSON.toJSONString(apiResult));
        return PushResult.fail();
    }

    final static String PLATFORM_NAME = "geTui";
}
