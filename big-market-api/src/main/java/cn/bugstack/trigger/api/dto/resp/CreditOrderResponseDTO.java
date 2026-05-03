package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 积分流水项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditOrderResponseDTO {

    /** 订单ID */
    private String orderId;

    /** 用户ID */
    private String userId;

    /** 交易名称 */
    private String tradeName;

    /** 交易类型 */
    private String tradeType;

    /** 交易金额 */
    private BigDecimal tradeAmount;

    /** 外部业务单号 */
    private String outBusinessNo;

    /** 创建时间 */
    private String createTime;

}
