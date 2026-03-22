package cn.bugstack.domain.credit.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreditAdjustSuccessMessage {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 业务仿重ID - 外部透传。返利、行为等唯一标识
     */
    private String outBusinessNo;

}
