package cn.bugstack.domain.activity.service.quota;

import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import cn.bugstack.domain.activity.service.quota.rule.IActivityChain;
import cn.bugstack.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.credit.model.valobj.TradeTypeVO;
import cn.bugstack.domain.strategy.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import cn.bugstack.domain.activity.model.entity.SkuRechargeEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.domain.strategy.model.entity.ActionChainModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {


    private ConfigurableApplicationContext applicationContext;

    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository,
                                              DefaultActivityChainFactory defaultActivityChainFactory,
                                              ConfigurableApplicationContext applicationContext) {
        super(activityRepository, defaultActivityChainFactory);
        this.applicationContext = applicationContext;
    }


    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1. 参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }

        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(skuRechargeEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        // 4. 活动规则校验
        IActivityChain activityChain = defaultActivityChainFactory.openActionChain();
        Boolean actionResult = activityChain.action(ActionChainModel
                .builder().activityEntity(activityEntity)
                .activitySkuEntity(activitySkuEntity)
                .activityCountEntity(activityCountEntity)
                .build());
        // 5. 构建聚合对象
        CreateQuotaOrderAggregate createOrderAggregate = this.buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);
        // 6. 按照交易类型 保存订单
        ITradePolicy tradePolicy = applicationContext.getBean(skuRechargeEntity.getOrderTradeType().getTradePolicy());
        tradePolicy.trade(createOrderAggregate);
        // 7. 返回单号

        return createOrderAggregate.getActivityOrderEntity().getOrderId();

    }


    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createOrderAggregate);


}
