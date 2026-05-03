package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserCreditAccountDao {

    int updateUserCreditAccount(UserCreditAccount userCreditAccount);

    void insert(UserCreditAccount userCreditAccount);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccount);

    List<UserCreditAccount> queryPage(int offset, int limit, String userId);

    int count(String userId);

}
