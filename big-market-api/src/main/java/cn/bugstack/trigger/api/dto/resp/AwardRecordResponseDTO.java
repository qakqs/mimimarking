package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中奖记录项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardRecordResponseDTO {

    /** 订单ID */
    private String orderId;

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;

    /** 策略ID */
    private Long strategyId;

    /** 奖品ID */
    private Integer awardId;

    /** 奖品标题 */
    private String awardTitle;

    /** 奖品配置 */
    private String awardConfig;

    /** 奖品状态 */
    private String awardState;

    /** 中奖时间 */
    private String awardTime;

}
