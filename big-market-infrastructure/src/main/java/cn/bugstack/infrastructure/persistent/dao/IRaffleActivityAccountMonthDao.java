package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface IRaffleActivityAccountMonthDao {
    int updateActivityAccountSubtractQuota(RaffleActivityAccountMonth build);

    void insert(RaffleActivityAccountMonth build);

    RaffleActivityAccountMonth queryActivityAccountMonthByUserId(RaffleActivityAccountMonth accountMonth);
}
