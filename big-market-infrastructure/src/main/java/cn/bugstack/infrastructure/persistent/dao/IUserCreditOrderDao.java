package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserCreditOrderDao {

    void insert(UserCreditOrder userCreditOrder);

    List<UserCreditOrder> queryPage(int offset, int limit, String userId);

    int count(String userId);

}
