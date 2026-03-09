package cn.bugstack.domain.activity.service.impl;

import cn.bugstack.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import cn.bugstack.infrastructure.persistent.repository.IActivityRepository;
import cn.bugstack.types.entity.ActivityCountEntity;
import cn.bugstack.types.entity.ActivityEntity;
import cn.bugstack.types.entity.ActivitySkuEntity;

public class RaffleActivitySupport {

    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository activityRepository;

    public RaffleActivitySupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }

}
