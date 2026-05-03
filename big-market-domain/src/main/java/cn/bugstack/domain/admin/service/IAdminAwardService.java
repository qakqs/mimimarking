package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminAwardEntity;

import java.util.List;

/**
 * 后台奖品管理服务接口
 */
public interface IAdminAwardService {

    void create(AdminAwardEntity entity);

    void update(AdminAwardEntity entity);

    void delete(Integer awardId);

    AdminAwardEntity detail(Integer awardId);

    List<AdminAwardEntity> list(int page, int pageSize, String awardDesc);

    int count(String awardDesc);

}
