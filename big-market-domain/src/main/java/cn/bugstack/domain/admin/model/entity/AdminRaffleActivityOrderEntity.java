package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理 - 抽奖活动订单实体（报表用）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRaffleActivityOrderEntity {

    private Long id;
    private String userId;
    private Long activityId;
    private Long sku;
    private String activityName;
    private Long strategyId;
    private String orderId;
    private Date orderTime;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private String state;
    private String outBusinessNo;
    private BigDecimal payAmount;
    private Date createTime;
    private Date updateTime;

}
