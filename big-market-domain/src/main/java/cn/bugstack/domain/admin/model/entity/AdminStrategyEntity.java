package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 策略实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStrategyEntity {

    private Long id;
    private Long strategyId;
    private String strategyDesc;
    private String ruleModels;
    private Date createTime;
    private Date updateTime;

}
