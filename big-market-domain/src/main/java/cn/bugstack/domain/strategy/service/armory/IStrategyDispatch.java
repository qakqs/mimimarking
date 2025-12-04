package cn.bugstack.domain.strategy.service.armory;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 策略抽奖调度
 * @create 2023-12-31 15:15
 */
public interface IStrategyDispatch {

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

}
