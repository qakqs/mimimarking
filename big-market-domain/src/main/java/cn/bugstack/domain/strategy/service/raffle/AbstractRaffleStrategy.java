package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleStock;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import static cn.bugstack.domain.strategy.service.rule.chain.LogicChainEnum.DEFAULT;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy, IRaffleStock {

    protected IStrategyRepository strategyRepository;

    protected IStrategyDispatch strategyDispatch;

    protected DefaultChainFactory defaultChainFactory;

    protected final DefaultTreeFactory defaultTreeFactory;
    @Resource
    private DefaultLogicFactory defaultLogicFactory;


    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory, DefaultLogicFactory defaultLogicFactory) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
        this.defaultLogicFactory = defaultLogicFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1 校验参数
        if (null == raffleFactorEntity.getStrategyId() || StringUtils.isEmpty(raffleFactorEntity.getStrategyId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode());
        }


        // 2 责任链抽奖计算(能不能抽奖)
        StrategyAwardVO chainStrategyAward = this.raffleLogicChain(raffleFactorEntity.getUserId(),
                raffleFactorEntity.getStrategyId());
        log.info("抽奖策略计算-责任链 {} {} {} {}", raffleFactorEntity.getUserId(), raffleFactorEntity.getStrategyId(), chainStrategyAward.getAwardId(), chainStrategyAward.getLogicModel());

        if (!DEFAULT.getChainName().equals(chainStrategyAward.getLogicModel())) {
            return RaffleAwardEntity.builder()
                    .awardId(chainStrategyAward.getAwardId())
                    .build();
        }

        // 2 抽奖结果过滤（能不能拿到手）
        StrategyAwardVO treeStrategyAward = this.raffleLogicTree(raffleFactorEntity.getUserId(),
                raffleFactorEntity.getStrategyId(), chainStrategyAward.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", raffleFactorEntity.getUserId(), raffleFactorEntity.getStrategyId(), treeStrategyAward.getAwardId(), treeStrategyAward.getLogicModel());


        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAward.getAwardId())
                .awardConfig(treeStrategyAward.getLogicModel())
                .build();
    }

    /**
     * 抽奖计算，责任链抽象方法
     *
     * @param userId
     * @param strategyId
     * @return
     */
    public abstract StrategyAwardVO raffleLogicChain(String userId, Long strategyId);


    /**
     * 抽奖结果过滤，决策树抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    public abstract StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);


}
