package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理 - 活动SKU实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminActivitySkuEntity {

    private Long sku;
    private Long activityId;
    private Long activityCountId;
    private Integer stockCount;
    private Integer stockCountSurplus;
    private BigDecimal productAmount;
    private Date createTime;
    private Date updateTime;

}
