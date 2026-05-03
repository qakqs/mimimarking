package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 积分手动调额请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAdjustRequestDTO {

    /** 用户ID */
    private String userId;

    /** 调整金额（正数增加，负数减少） */
    private BigDecimal amount;

    /** 调额原因 */
    private String reason;

}
