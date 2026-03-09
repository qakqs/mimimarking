package cn.bugstack.test.domain;

import cn.bugstack.Application;
import cn.bugstack.domain.activity.service.IRaffleOrder;
import cn.bugstack.types.entity.SkuRechargeEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)

public class RaffleOrderTest {

    @Resource
    private IRaffleOrder raffleOrder;

@Test
public void test_createSkuRechargeOrder() {
    SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
    skuRechargeEntity.setUserId("mp");
    skuRechargeEntity.setSku(9011L);
    // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
    skuRechargeEntity.setOutBusinessNo("700091009111");
    String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
    log.info("测试结果：{}", orderId);
}

}
