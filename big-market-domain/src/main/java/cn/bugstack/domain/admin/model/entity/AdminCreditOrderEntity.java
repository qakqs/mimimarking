package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理 - 积分流水实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreditOrderEntity {

    private Long id;
    private String userId;
    private String orderId;
    private String tradeName;
    private String tradeType;
    private BigDecimal tradeAmount;
    private String outBusinessNo;
    private Date createTime;
    private Date updateTime;

}
