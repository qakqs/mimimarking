package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 DAO
 */
@Mapper
public interface IUserDao {

    void insert(User userPO);

    User queryByUsername(String username);

    User queryByUserId(String userId);

    int update(User userPO);

    List<User> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                         @Param("keyword") String keyword, @Param("status") String status);

    int count(@Param("keyword") String keyword, @Param("status") String status);

    int updateStatus(@Param("userId") String userId, @Param("status") Integer status);

}
