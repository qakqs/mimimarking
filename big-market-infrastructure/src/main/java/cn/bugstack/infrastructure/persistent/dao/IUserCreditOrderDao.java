package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserCreditOrderDao {

    void insert(UserCreditOrder userCreditOrder);
}
