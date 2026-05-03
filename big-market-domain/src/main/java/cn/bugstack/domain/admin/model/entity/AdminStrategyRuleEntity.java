package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 策略规则实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStrategyRuleEntity {

    private Long id;
    private Long strategyId;
    private Integer awardId;
    private Integer ruleType;
    private String ruleModel;
    private String ruleValue;
    private String ruleDesc;
    private Date createTime;
    private Date updateTime;

}
