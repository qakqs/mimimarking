package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminAwardEntity;

import java.util.List;

/**
 * 后台管理 - 奖品仓储接口
 */
public interface IAdminAwardRepository {

    void saveAward(AdminAwardEntity entity);

    void updateAward(AdminAwardEntity entity);

    void deleteAward(Integer awardId);

    AdminAwardEntity queryAwardById(Integer awardId);

    List<AdminAwardEntity> queryAwardPage(int offset, int limit, String awardDesc);

    int countAward(String awardDesc);

}
