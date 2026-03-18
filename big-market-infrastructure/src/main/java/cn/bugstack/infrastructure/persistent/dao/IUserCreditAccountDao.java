package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserCreditAccountDao {
    int updateUserCreditAccount(UserCreditAccount userCreditAccount);

    void insert(UserCreditAccount userCreditAccount);

}
