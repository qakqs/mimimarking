package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccountDay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityAccountDayDao {
    int updateActivityAccountSubtractQuota(RaffleActivityAccountDay build);

    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);

    void insert(RaffleActivityAccountDay build);

    Integer queryRaffleActivityAccountDayPartakeCount(RaffleActivityAccountDay raffleActivityAccountDayReq);
}
