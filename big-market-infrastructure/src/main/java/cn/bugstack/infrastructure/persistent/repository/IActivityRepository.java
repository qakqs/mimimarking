package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.types.aggregate.CreateOrderAggregate;
import cn.bugstack.types.entity.ActivityCountEntity;
import cn.bugstack.types.entity.ActivityEntity;
import cn.bugstack.types.entity.ActivitySkuEntity;
import cn.bugstack.types.vo.ActivitySkuStockKeyVO;

import java.util.Date;

public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO build);

    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
