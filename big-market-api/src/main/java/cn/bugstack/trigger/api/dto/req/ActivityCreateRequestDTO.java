package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建/编辑活动请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCreateRequestDTO {

    /** 活动ID（编辑时传入） */
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

}
