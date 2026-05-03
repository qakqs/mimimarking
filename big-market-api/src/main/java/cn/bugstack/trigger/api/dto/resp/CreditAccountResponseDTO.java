package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 积分账户响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountResponseDTO {

    /** 用户ID */
    private String userId;

    /** 当前积分余额 */
    private BigDecimal adjustAmount;

}
