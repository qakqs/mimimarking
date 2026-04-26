package cn.bugstack.test;

import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import cn.bugstack.domain.task.service.impl.OrderAuditEngine;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.beancontext.BeanContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private OrderAuditEngine orderAuditEngine;

    @Test
    public void test() {

        for (int j = 0; j < 3; j++) {
            List<List<String>> batches = new ArrayList<>();
            batches.add(Arrays.asList("1"));
            batches.add(Arrays.asList("ASasAS"));
            for (int i = 0; i < 1000; i++) {
                batches.add(Arrays.asList("" + i));
            }
            List<String> strings = orderAuditEngine.auditOrders(batches);
            log.info(String.valueOf(strings.size()));
        }

        log.info("结束");
    }

}
