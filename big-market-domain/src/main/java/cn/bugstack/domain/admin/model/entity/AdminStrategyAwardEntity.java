package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台管理 - 策略奖品实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStrategyAwardEntity {

    private Long id;
    private Long strategyId;
    private Integer awardId;
    private String awardTitle;
    private String awardSubtitle;
    private Integer awardCount;
    private Integer awardCountSurplus;
    private BigDecimal awardRate;
    private String ruleModels;
    private Integer sort;
    private Date createTime;
    private Date updateTime;

}
