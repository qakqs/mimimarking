package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.types.aggregate.CreateOrderAggregate;
import cn.bugstack.types.entity.ActivityCountEntity;
import cn.bugstack.types.entity.ActivityEntity;
import cn.bugstack.types.entity.ActivitySkuEntity;

public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}
