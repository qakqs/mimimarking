package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminAwardEntity;
import cn.bugstack.domain.admin.repository.IAdminAwardRepository;
import cn.bugstack.infrastructure.persistent.dao.IAwardDao;
import cn.bugstack.infrastructure.persistent.po.Award;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 奖品仓储实现
 */
@Repository
public class AdminAwardRepository implements IAdminAwardRepository {

    @Resource
    private IAwardDao awardDao;

    @Override
    public void saveAward(AdminAwardEntity entity) {
        awardDao.insert(toAwardPO(entity));
    }

    @Override
    public void updateAward(AdminAwardEntity entity) {
        awardDao.update(toAwardPO(entity));
    }

    @Override
    public void deleteAward(Integer awardId) {
        awardDao.deleteByAwardId(awardId);
    }

    @Override
    public AdminAwardEntity queryAwardById(Integer awardId) {
        // use queryAwardList and filter, since no single query by ID method
        // alternatively, add a queryById method to the DAO
        Award po = awardDao.queryAwardById(awardId);
        return toAwardEntity(po);
    }

    @Override
    public List<AdminAwardEntity> queryAwardPage(int offset, int limit, String awardDesc) {
        List<Award> list = awardDao.queryAwardPage(offset, limit, awardDesc);
        return list.stream().map(this::toAwardEntity).collect(Collectors.toList());
    }

    @Override
    public int countAward(String awardDesc) {
        return awardDao.countAward(awardDesc);
    }

    // ===== PO <-> Entity mapping =====

    private Award toAwardPO(AdminAwardEntity e) {
        Award po = new Award();
        po.setId(e.getId()); po.setAwardId(e.getAwardId());
        po.setAwardKey(e.getAwardKey()); po.setAwardConfig(e.getAwardConfig());
        po.setAwardDesc(e.getAwardDesc());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminAwardEntity toAwardEntity(Award po) {
        if (po == null) return null;
        return AdminAwardEntity.builder()
                .id(po.getId()).awardId(po.getAwardId()).awardKey(po.getAwardKey())
                .awardConfig(po.getAwardConfig()).awardDesc(po.getAwardDesc())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
