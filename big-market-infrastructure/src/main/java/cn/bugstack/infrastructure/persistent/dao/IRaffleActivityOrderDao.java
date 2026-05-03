package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRaffleActivityOrderDao {

    int insert(RaffleActivityOrder raffleActivityOrder);

    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId();

    RaffleActivityOrder queryRaffleActivityOrder(RaffleActivityOrder req);

    int updateOrderCompleted(RaffleActivityOrder req);

    RaffleActivityOrder queryUnpayActivityOrder(RaffleActivityOrder req);

    List<RaffleActivityOrder> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                                        @Param("userId") String userId, @Param("activityId") Long activityId,
                                        @Param("orderState") String orderState);

    int count(@Param("userId") String userId, @Param("activityId") Long activityId,
              @Param("orderState") String orderState);

}
