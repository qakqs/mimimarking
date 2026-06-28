package cn.bugstack.trigger.job;

import cn.bugstack.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import cn.bugstack.domain.strategy.service.IRaffleStock;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 更新奖品库存任务
 */

@Component
public class UpdateAwardStockJob {
    private static final Log log = Log.get(UpdateAwardStockJob.class);

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void process() {
        try {

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
