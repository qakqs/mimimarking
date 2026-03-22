package cn.bugstack.test.domain;

import cn.bugstack.Application;
import cn.bugstack.domain.activity.model.entity.SkuRechargeEntity;
import cn.bugstack.domain.activity.model.valobj.OrderTradeTypeVO;
import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.activity.service.IRaffleActivityPartakeService;
import cn.bugstack.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.bugstack.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.service.IAwardService;
import cn.bugstack.domain.credit.model.entity.TradeEntity;
import cn.bugstack.domain.credit.model.valobj.TradeNameVO;
import cn.bugstack.domain.credit.model.valobj.TradeTypeVO;
import cn.bugstack.domain.credit.service.ICreditAdjustService;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)

public class RaffleActivityPartakeServiceTest {
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    private IAwardService awardService;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @Resource
    private ICreditAdjustService creditAdjustService;
    @Test
    public void test_createOrder() {
        // 请求参数
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        // 调用接口
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrder));
    }


    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("xiaofuge");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardState(AwardStateVO.create);
            awardService.saveUserAwardRecord(userAwardRecordEntity);
        }
    }

    /**
     * 商品下单
     */
    @Test
    public void test_credit_pay_trade() {
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("xiaofuge");
        skuRechargeEntity.setSku(9011L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        skuRechargeEntity.setOutBusinessNo("70009240608007");
        skuRechargeEntity.setOrderTradeType(OrderTradeTypeVO.credit_pay_trade);
        String orderId = raffleActivityAccountQuotaService.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }

    @Test
    public void test_createOrder_pay() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("70009240608007");
        creditAdjustService.createOrder(tradeEntity);
        new CountDownLatch(1).await();
    }


}
