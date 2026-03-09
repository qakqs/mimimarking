package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRaffleActivityOrderDao {
    int insert();

    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId();
}
