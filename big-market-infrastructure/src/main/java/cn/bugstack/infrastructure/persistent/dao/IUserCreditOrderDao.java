package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IUserCreditOrderDao {

    void insert(UserCreditOrder userCreditOrder);

    List<UserCreditOrder> queryPage(@Param("offset") int offset, @Param("limit") int limit,
                                    @Param("userId") String userId, @Param("tradeType") String tradeType);

    int count(@Param("userId") String userId, @Param("tradeType") String tradeType);

}
