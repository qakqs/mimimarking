package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserAwardRecordDao {
    void insert(UserAwardRecord userAwardRecord);
}
