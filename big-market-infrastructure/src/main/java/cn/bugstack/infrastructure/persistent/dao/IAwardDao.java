package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();

    String queryAwardConfigAwardId(Integer awardId);

    String queryAwardKey(Integer awardId);

    int insert(Award po);

    int update(Award po);

    int deleteByAwardId(@Param("awardId") Integer awardId);

    Award queryAwardById(@Param("awardId") Integer awardId);

    List<Award> queryAwardPage(@Param("offset") int offset, @Param("limit") int limit,
                               @Param("awardDesc") String awardDesc);

    int countAward(@Param("awardDesc") String awardDesc);

}
