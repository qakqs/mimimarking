package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略规则保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleSaveRequestDTO {

    /** 自增ID（编辑时传入） */
    private Long id;

    /** 策略ID */
    private Long strategyId;

    /** 奖品ID（策略规则不用，奖品规则需要） */
    private Integer awardId;

    /** 规则类型：1-策略规则、2-奖品规则 */
    private Integer ruleType;

    /** 规则模型 */
    private String ruleModel;

    /** 规则值 */
    private String ruleValue;

    /** 规则描述 */
    private String ruleDesc;

}
