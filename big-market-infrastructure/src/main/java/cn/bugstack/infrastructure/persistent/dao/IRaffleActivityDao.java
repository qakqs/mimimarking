package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRaffleActivityDao {

    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);

    Long queryStrategyIdByActivityId(Long activityId);

    int insert(RaffleActivity po);

    int update(RaffleActivity po);

    int deleteByActivityId(@Param("activityId") Long activityId);

    List<RaffleActivity> queryActivityPage(@Param("offset") int offset, @Param("limit") int limit,
                                           @Param("activityName") String activityName, @Param("state") String state);

    int countActivity(@Param("activityName") String activityName, @Param("state") String state);

    int updateActivityStatus(@Param("activityId") Long activityId, @Param("state") Integer state);

    Integer queryMaxActivityId();
}
