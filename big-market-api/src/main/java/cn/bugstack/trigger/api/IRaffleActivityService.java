package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.ActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.resp.ActivityDrawResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

public interface IRaffleActivityService {

    /**
     * 活动装配，数据预热缓存
     *
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     *
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);


    /**
     * 日历签到返利接口
     * @param userId 用户id
     * @return 成功或失败
     */
    Response<Boolean> calenderSignRebate(String userId);

}
