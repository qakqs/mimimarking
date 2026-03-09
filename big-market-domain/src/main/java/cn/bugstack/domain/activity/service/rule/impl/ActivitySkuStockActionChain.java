package cn.bugstack.domain.activity.service.rule.impl;

import cn.bugstack.types.model.ActionChainModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActivitySkuStockActionChain extends AbstractActivityChain {
    @Override
    public Boolean action(ActionChainModel actionChainModel) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始");
        return true;
    }
}
