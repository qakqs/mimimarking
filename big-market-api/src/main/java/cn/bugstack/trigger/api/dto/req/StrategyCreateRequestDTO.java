package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建/编辑策略请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyCreateRequestDTO {

    /** 策略ID（编辑时传入） */
    private Long strategyId;

    /** 策略描述 */
    private String strategyDesc;

    /** 规则模型列表（逗号分隔，如 rule_blacklist,rule_weight） */
    private String ruleModels;

}
