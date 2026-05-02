package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 DAO
 */
@Mapper
public interface IUserDao {

    void insert(User userPO);

    User queryByUsername(String username);

    User queryByUserId(String userId);

    int update(User userPO);

}
