package cn.bugstack.trigger.job;

import cn.bugstack.domain.activity.service.IRaffleActivitySkuStockService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class UpdateActivitySkuStockJob {

    @Resource
    private IRaffleActivitySkuStockService skuStock;

}
