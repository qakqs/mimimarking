package cn.bugstack.types.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LogicTreeNodeVO {

    String userId;
    Long strategyId;
    Integer awardId;
    String ruleValue;
}
