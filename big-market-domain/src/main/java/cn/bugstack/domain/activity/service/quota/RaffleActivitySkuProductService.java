package cn.bugstack.domain.activity.service.quota;

import cn.bugstack.domain.activity.model.entity.SkuProductEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.IRaffleActivitySkuProductService;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {
    private static final Log log = Log.get(RaffleActivitySkuProductService.class);
    @Resource
    private IActivityRepository repository;

    @Override
    public List<SkuProductEntity> querySkuProductListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }
}
