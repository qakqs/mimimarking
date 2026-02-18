package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {

    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略id
         */
        private Long strategyId;

        /**
         * 权重值key，用于抽奖时可以选择抽奖
         */
        private String ruleWeightValueKey;
        /**
         * 奖品id
         */
        private Integer awardId;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    static public class RaffleCenterEntity extends RaffleEntity {
        /**
         * 策略id
         */
        private Long strategyId;

        /**
         * 权重值key，用于抽奖时可以选择抽奖
         */
        private String ruleWeightValueKey;
        /**
         * 奖品id
         */
        private Integer awardId;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    static public class RaffleAfterEntity extends RaffleEntity {

    }

}
