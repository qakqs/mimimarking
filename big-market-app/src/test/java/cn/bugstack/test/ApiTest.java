package cn.bugstack.test;

import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.beancontext.BeanContext;
import java.util.Map;

import static cn.bugstack.domain.activity.model.valobj.OrderTradeTypeVO.credit_pay_trade;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 功能测试
 * @create 2023-12-23 11:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    ConfigurableApplicationContext applicationContext;
    @Test
    public void test() {

        log.info("");
    }

}
