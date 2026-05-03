package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 策略奖品保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardSaveRequestDTO {

    /** 自增ID（编辑时传入） */
    private Long id;

    /** 策略ID */
    private Long strategyId;

    /** 奖品ID */
    private Integer awardId;

    /** 奖品标题 */
    private String awardTitle;

    /** 奖品副标题 */
    private String awardSubtitle;

    /** 奖品库存总量 */
    private Integer awardCount;

    /** 奖品中奖概率 */
    private BigDecimal awardRate;

    /** 规则模型 */
    private String ruleModels;

    /** 排序 */
    private Integer sort;

}
