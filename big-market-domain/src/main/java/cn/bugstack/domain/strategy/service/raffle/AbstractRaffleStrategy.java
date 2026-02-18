package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.model.vo.LogicModel;
import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository strategyRepository;

    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        if (null == raffleFactorEntity.getStrategyId() || StringUtils.isEmpty(raffleFactorEntity.getStrategyId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode());
        }
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(raffleFactorEntity.getStrategyId());
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder()
                .userId(raffleFactorEntity.getUserId())
                .strategyId(raffleFactorEntity.getStrategyId()).build(), strategyEntity.ruleModels());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode()) &&
                LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
            // 黑名单返回固定的奖品ID
            return RaffleAwardEntity.builder()
                    .awardId(ruleActionEntity.getData().getAwardId())
                    .build();

        }

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode()) &&
                LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
            // 权重根据返回的信息进行抽奖
            RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
            String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
            Integer awardId = strategyDispatch.getRandomAwardId(raffleFactorEntity.getStrategyId(), ruleWeightValueKey);
            return RaffleAwardEntity.builder()
                    .awardId(awardId)
                    .build();

        }

        // 4. 默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId(raffleFactorEntity.getStrategyId());

        // 5. 查询抽奖规则
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository
                .queryStrategyAwardRuleModel(raffleFactorEntity.getStrategyId(), awardId);
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> centerEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                        .userId(raffleFactorEntity.getUserId())
                        .strategyId(raffleFactorEntity.getStrategyId())
                        .awardId(awardId)
                        .build()
                , strategyAwardRuleModelVO.raffleCenterRuleModelList());

    if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(centerEntity.getCode())){
        log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
        return RaffleAwardEntity.builder()
                .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                .build();
    }
    return RaffleAwardEntity.builder()
            .awardId(awardId)
            .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity build, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity build, String... logics);


}
