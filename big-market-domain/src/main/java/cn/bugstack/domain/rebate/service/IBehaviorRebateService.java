package cn.bugstack.domain.rebate.service;

import cn.bugstack.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * 行为返利服务接口
 */
public interface IBehaviorRebateService {

    /**
     *  创建行为返利订单
     * @param behaviorEntity
     * @return
     */
    List<String> createOrder(BehaviorEntity  behaviorEntity);
}
