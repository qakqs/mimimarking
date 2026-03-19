package cn.bugstack.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreditAccountEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 可用积分，每次扣减的值
     */
    private BigDecimal adjustAmount;
}
