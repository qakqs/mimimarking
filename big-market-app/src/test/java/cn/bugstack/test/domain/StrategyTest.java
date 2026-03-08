package cn.bugstack.test.domain;

import cn.bugstack.Application;
import cn.bugstack.types.entity.RaffleAwardEntity;
import cn.bugstack.types.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.domain.strategy.service.raffle.DefaultRaffleStrategy;
import cn.bugstack.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import cn.bugstack.domain.strategy.service.rule.tree.impl.RuleLockLogicTreeNode;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 策略领域测试
 * @create 2023-12-23 11:33
 */
@Slf4j
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
public class StrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private DefaultRaffleStrategy raffleStrategy;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;

    @Resource
    private RuleLockLogicTreeNode ruleLockLogicTreeNode;

    @Before
    public void setUp() {
        log.info("setUp测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);
        ReflectionTestUtils.setField(ruleLockLogicTreeNode, "userRaffleCount", 0L);
    }

    @Test
    public void test_performRaffle() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("xiaofuge")
                    .strategyId(100006L)
                    .build();
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
        // 等待 UpdateAwardStockJob 消费队列
        new CountDownLatch(1).await();
    }
}
