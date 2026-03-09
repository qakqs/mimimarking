package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityCount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityCountDao {
    public RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
