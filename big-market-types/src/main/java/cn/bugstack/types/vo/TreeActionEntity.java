package cn.bugstack.types.vo;

import cn.bugstack.types.enums.RuleLogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TreeActionEntity {


    private RuleLogicCheckTypeVO ruleLogicCheckType;
    private StrategyAwardData strategyAwardData;
}
