package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RaffleStrategyRuleWeightResponseDTO {

    /**
     *   权重规则配置的抽奖次数
     */
    private Integer ruleWeightCount;

    /**
     * 用户在一个活动下完成的总抽奖次数
     */
    private Integer useActivityAccountTotalUseCount;

    /**
     * 当前权重可抽奖范围
     */
    private List<StrategyAward> strategyAwards;

}
