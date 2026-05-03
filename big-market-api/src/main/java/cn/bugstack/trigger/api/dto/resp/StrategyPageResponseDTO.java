package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略分页列表项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyPageResponseDTO {

    /** 策略ID */
    private Long strategyId;

    /** 策略描述 */
    private String strategyDesc;

    /** 规则模型列表 */
    private String ruleModels;

    /** 创建时间 */
    private String createTime;

}
