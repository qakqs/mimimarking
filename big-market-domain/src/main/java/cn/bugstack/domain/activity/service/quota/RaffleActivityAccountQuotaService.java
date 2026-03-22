package cn.bugstack.domain.activity.service.quota;

import cn.bugstack.domain.activity.model.entity.*;
import cn.bugstack.domain.activity.service.IRaffleActivitySkuStockService;
import cn.bugstack.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import cn.bugstack.domain.strategy.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.domain.activity.model.valobj.OrderStateVO;
import cn.bugstack.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RaffleActivityAccountQuotaService extends AbstractRaffleActivityAccountQuota implements IRaffleActivitySkuStockService {
    public RaffleActivityAccountQuotaService(IActivityRepository activityRepository,
                                             DefaultActivityChainFactory defaultActivityChainFactory,
                                             ConfigurableApplicationContext applicationContext) {
        super(activityRepository, defaultActivityChainFactory, applicationContext);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity,
                                                            ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity,
                                                            ActivityCountEntity activityCountEntity) {

        // 订单实体对象
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
        activityOrderEntity.setUserId(skuRechargeEntity.getUserId());
        activityOrderEntity.setSku(skuRechargeEntity.getSku());
        activityOrderEntity.setActivityId(activityEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());
        // 公司里一般会有专门的雪花算法UUID服务，我们这里直接生成个12位就可以了。
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setState(OrderStateVO.completed);
        activityOrderEntity.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());

        // 构建聚合对象
        return CreateQuotaOrderAggregate.builder()
                .userId(skuRechargeEntity.getUserId())
                .activityId(activitySkuEntity.getActivityId())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .activityOrderEntity(activityOrderEntity)
                .build();
    }

    @Override
    protected void doSaveOrder(CreateQuotaOrderAggregate createOrderAggregate) {
        activityRepository.doSaveCreditNoPayOrder(createOrderAggregate);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void clearQueueValue() {
        activityRepository.clearQueueValue();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        activityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        activityRepository.clearActivitySkuStock(sku);
    }

    @Override
    public void updateSkuRechargeOrder(DeliveryOrderEntity deliveryOrderEntity) {
        activityRepository.updateSkuRechargeOrder(deliveryOrderEntity);

    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {
       return activityRepository.queryRaffleActivityAccountDayPartakeCount(activityId, userId);
    }

    @Override
    public ActivityAccountEntity queryActivityAccount(String userId, Long activityId) {
       return  activityRepository.queryActivityAccount(userId, activityId);
    }

    @Override
    public Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId) {
        return activityRepository.queryRaffleActivityAccountPartakeCount(activityId, userId);
    }
}
