package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 抽奖订单查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderPageRequestDTO extends PageRequestDTO {

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;

    /** 订单状态 */
    private String orderState;

}
