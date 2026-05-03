package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);

    int insert(Strategy po);

    int update(Strategy po);

    int deleteByStrategyId(@Param("strategyId") Long strategyId);

    List<Strategy> queryStrategyPage(@Param("offset") int offset, @Param("limit") int limit,
                                     @Param("strategyDesc") String strategyDesc);

    int countStrategy(@Param("strategyDesc") String strategyDesc);

}
