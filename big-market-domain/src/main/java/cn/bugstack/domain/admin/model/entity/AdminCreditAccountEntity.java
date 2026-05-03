package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理 - 积分账户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreditAccountEntity {

    private Long id;
    private String userId;
    private BigDecimal totalAmount;
    private BigDecimal availableAmount;
    private String accountStatus;
    private Date createTime;
    private Date updateTime;

}
