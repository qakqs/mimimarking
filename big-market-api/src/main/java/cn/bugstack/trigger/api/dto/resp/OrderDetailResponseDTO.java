package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖订单详情响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {

    /** 订单ID */
    private String orderId;

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 策略ID */
    private Long strategyId;

    /** 订单状态 */
    private String orderState;

    /** 订单时间 */
    private String orderTime;

    /** 活动结束时间 */
    private String endDateTime;

}
