package cn.bugstack.test.domain;

import cn.bugstack.Application;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.raffle.DefaultRaffleStrategy;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.chain.impl.BackListLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.DefaultLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)

public class treeTest {

        @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private DefaultRaffleStrategy raffleStrategy;

    @Resource
    private DefaultLogicChain defaultLogicChain;
    @Resource
    private DefaultChainFactory defaultChainFactory;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private BackListLogicChain backListLogicChain;

    @Resource
    private IRedisService redisService;
    @Resource
    private DefaultTreeFactory defaultTreeFactory;
@Before
public void setUp() {
    log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
    log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
    // 通过反射 mock 规则中的值
    ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);
}

@Test
public void test_performRaffle() {
    RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
            .userId("xiaofuge")
            .strategyId(100006L)
            .build();
    RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
    log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
    log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
}
}
