package cn.bugstack.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LogicTreeNodeVO {

    String userId;
    Long strategyId;
    Integer awardId;
    String ruleValue;
    Date endDateTime;
}
