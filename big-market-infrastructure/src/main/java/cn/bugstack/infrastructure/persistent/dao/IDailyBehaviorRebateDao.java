package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.DailyBehaviorRebate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IDailyBehaviorRebateDao {

    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String behaviorType);

    int insert(DailyBehaviorRebate po);

    int deleteById(@Param("id") Long id);

    int updateState(@Param("id") Long id, @Param("state") String state);

    List<DailyBehaviorRebate> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                                        @Param("behaviorType") String behaviorType,
                                        @Param("state") String state);

    int count(@Param("behaviorType") String behaviorType, @Param("state") String state);

}
