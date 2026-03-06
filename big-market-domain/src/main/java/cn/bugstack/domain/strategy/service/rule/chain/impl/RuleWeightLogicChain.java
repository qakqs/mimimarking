package cn.bugstack.domain.strategy.service.rule.chain.impl;

import cn.bugstack.domain.strategy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.LogicChainEnum;
import cn.bugstack.types.common.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class RuleWeightLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;

    @Resource
    protected IStrategyDispatch strategyDispatch;

    // 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
    public Long userScore = 0L;


    @Override
    public StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleModel());
        String ruleValue = strategyRuleEntity.getRuleValue();

        // 1. 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) return null;

        // 2. 转换Keys值，并默认排序
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        // 3. 找出最小符合的值，也就是【4500 积分，能找到 4000:102,103,104,105】、【5000 积分，能找到 5000:102,103,104,105,106,107】
        /* 找到最后一个符合的值[如用户传了一个 5900 应该返回正确结果为 5000]，如果使用 Lambda findFirst 需要注意使用 sorted 反转结果
         *   Long nextValue = null;
         *         for (Long analyticalSortedKeyValue : analyticalSortedKeys) {
         *             if (userScore >= analyticalSortedKeyValue){
         *                 nextValue = analyticalSortedKeyValue;
         *             }
         *         }
         * 星球伙伴 @慢慢来 ID 6267 提供
         * Long nextValue = analyticalSortedKeys.stream()
         *      .filter(key -> userScore >= key)
         *      .max(Comparator.naturalOrder())
         *      .orElse(null);
         */
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        // 4. 权重抽奖
        if (null != nextValue) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return StrategyAwardVO.builder().logicModel(ruleModel()).awardId(awardId).build();
        }

        // 5. 过滤其他责任链
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }

    @Override
    protected String ruleModel() {
        return LogicChainEnum.RULE_WEIGHT.getChainName();
    }

}
