package cn.bugstack.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RuleWeightVO {

    private String ruleValue;

    private Integer weight;
    private List<Integer> awardIds;
    private List<Award> awardList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public static class Award {
        private Integer awardId;
        private String awardTitle;

    }
}
