package cn.bugstack.trigger.job;

import cn.bugstack.types.vo.StrategyAwardStockKeyVO;
import cn.bugstack.domain.strategy.service.IRaffleStock;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 更新奖品库存任务
 */

@Slf4j
@Component
public class UpdateAwardStockJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void process() {
        try {
            log.info("定时任务 更新奖品消耗库存 start ");

            StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if (strategyAwardStockKeyVO == null) {
                return;
            }
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            log.info("定时任务 更新奖品消耗库存 success strategyId:{} awardId:{}",
                    strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());

        } catch (Exception e) {
            log.error("定时任务，更新奖品失败", e);
        }

    }

}
