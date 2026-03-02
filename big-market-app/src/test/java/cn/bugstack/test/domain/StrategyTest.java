package cn.bugstack.test.domain;

import cn.bugstack.Application;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.raffle.DefaultRaffleStrategy;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.chain.impl.BackListLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.DefaultLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.annotation.*;
import java.util.*;

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


    @Test
    public void test_performRaffle() {

        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("xiaofuge")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_LogicChain_rule_blacklist() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        Integer awardId = logicChain.logic("user001", 100001L);
        log.info("测试结果：{}", awardId);
    }

    @Test
    public void test_LogicChain_rule_weight() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        Integer awardId = logicChain.logic("xiaofuge", 100001L);
        log.info("测试结果：{}", awardId);
    }

    @Test
    public void test_LogicChain_rule_default() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        Integer awardId = logicChain.logic("xiaofuge", 100001L);
        log.info("测试结果：{}", awardId);
    }


}
