package cn.bugstack.domain.activity.service;


import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.domain.activity.model.entity.DeliveryOrderEntity;
import cn.bugstack.domain.activity.model.entity.SkuRechargeEntity;

/**
 * 抽奖活动账户额度服务
 */
public interface IRaffleActivityAccountQuotaService {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param skuRechargeEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity);

    /**
     * 更新抽奖活动订单订单
     * @param deliveryOrderEntity
     * @return
     */
    void updateSkuRechargeOrder(DeliveryOrderEntity deliveryOrderEntity);

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    ActivityAccountEntity queryActivityAccount(String userId, Long activityId);

    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);

}
