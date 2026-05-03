package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动分页列表项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPageResponseDTO {

    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 活动描述 */
    private String activityDesc;

    /** 开始时间 */
    private String beginDateTime;

    /** 结束时间 */
    private String endDateTime;

    /** 抽奖策略ID */
    private Long strategyId;

    /** 活动状态 */
    private String state;

    /** 创建时间 */
    private String createTime;

}
