package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 返利订单实体（报表用）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRebateOrderEntity {

    private Long id;
    private String userId;
    private String orderId;
    private String outBusinessNo;
    private String behaviorType;
    private String rebateDesc;
    private String rebateType;
    private String rebateConfig;
    private String bizId;
    private Date createTime;
    private Date updateTime;

}
