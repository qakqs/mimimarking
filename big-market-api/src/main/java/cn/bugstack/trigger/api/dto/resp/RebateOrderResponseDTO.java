package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返利订单项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateOrderResponseDTO {

    /** 订单ID */
    private String orderId;

    /** 用户ID */
    private String userId;

    /** 行为类型 */
    private String behaviorType;

    /** 返利类型 */
    private String rebateType;

    /** 返利配置 */
    private String rebateConfig;

    /** 外部业务单号 */
    private String outBusinessNo;

    /** 创建时间 */
    private String createTime;

}
