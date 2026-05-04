package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IUserAwardRecordDao {

    void insert(UserAwardRecord userAwardRecord);

    int updateAwardRecordCompletedState(UserAwardRecord userAwardRecord);

    List<UserAwardRecord> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                                    @Param("userId") String userId,
                                    @Param("activityId") Long activityId, @Param("awardState") String awardState);

    int count(@Param("userId") String userId,
              @Param("activityId") Long activityId, @Param("awardState") String awardState);

    List<Map<String, Object>> queryAwardStat(@Param("activityId") Long activityId);

}
