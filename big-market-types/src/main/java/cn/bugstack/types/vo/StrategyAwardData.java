package cn.bugstack.types.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class StrategyAwardData {

    private Integer awardId;

    private String awardRuleValue;
}
