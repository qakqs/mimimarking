package cn.bugstack.domain.activity.service.quota.rule.impl;

import cn.bugstack.domain.activity.service.armory.IActivityDispatch;
import cn.bugstack.infrastructure.persistent.repository.IActivityRepository;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.types.model.ActionChainModel;
import cn.bugstack.types.vo.ActivitySkuStockKeyVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActivitySkuStockActionChain extends AbstractActivityChain {

    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public Boolean action(ActionChainModel actionChainModel) {
        log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】开始。sku:{} activityId:{}",
                actionChainModel.getActivitySkuEntity().getSku(), actionChainModel.getActivityEntity().getActivityId());

        boolean lockResult = activityDispatch.subtractionActivitySkuStock(actionChainModel.getActivitySkuEntity().getSku(),
                actionChainModel.getActivityEntity().getEndDateTime());
        if (lockResult) {
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO
                    .builder()
                    .sku(actionChainModel.getActivitySkuEntity().getSku())
                    .activityId(actionChainModel.getActivityEntity().getActivityId())
                    .build()
            );
            return true;
        }
        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR);

    }
}
