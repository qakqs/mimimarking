package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;
import cn.bugstack.domain.admin.repository.IAdminCreditRepository;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditAccountDao;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditOrderDao;
import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 积分仓储实现
 */
@Repository
public class AdminCreditRepository implements IAdminCreditRepository {

    @Resource
    private IUserCreditAccountDao userCreditAccountDao;

    @Resource
    private IUserCreditOrderDao userCreditOrderDao;

    @Override
    public List<AdminCreditAccountEntity> queryCreditAccountPage(int offset, int limit, String userId) {
        List<UserCreditAccount> list = userCreditAccountDao.queryPage(offset, limit, userId);
        return list.stream().map(this::toAccountEntity).collect(Collectors.toList());
    }

    @Override
    public int countCreditAccount(String userId) {
        return userCreditAccountDao.count(userId);
    }

    @Override
    public AdminCreditAccountEntity queryCreditAccountByUserId(String userId) {
        UserCreditAccount req = new UserCreditAccount();
        req.setUserId(userId);
        UserCreditAccount po = userCreditAccountDao.queryUserCreditAccount(req);
        return toAccountEntity(po);
    }

    @Override
    public List<AdminCreditOrderEntity> queryCreditOrderPage(int offset, int limit, String userId) {
        List<UserCreditOrder> list = userCreditOrderDao.queryPage(offset, limit, userId);
        return list.stream().map(this::toOrderEntity).collect(Collectors.toList());
    }

    @Override
    public int countCreditOrder(String userId) {
        return userCreditOrderDao.count(userId);
    }

    @Override
    public void insertCreditOrder(AdminCreditOrderEntity entity) {
        userCreditOrderDao.insert(toCreditOrderPO(entity));
    }

    @Override
    public void updateCreditAccount(AdminCreditAccountEntity entity) {
        userCreditAccountDao.updateUserCreditAccount(toCreditAccountPO(entity));
    }

    private UserCreditAccount toCreditAccountPO(AdminCreditAccountEntity e) {
        return UserCreditAccount.builder()
                .id(e.getId()).userId(e.getUserId())
                .totalAmount(e.getTotalAmount()).availableAmount(e.getAvailableAmount())
                .accountStatus(e.getAccountStatus())
                .createTime(e.getCreateTime()).updateTime(e.getUpdateTime()).build();
    }

    private AdminCreditAccountEntity toAccountEntity(UserCreditAccount po) {
        if (po == null) return null;
        return AdminCreditAccountEntity.builder()
                .id(po.getId()).userId(po.getUserId())
                .totalAmount(po.getTotalAmount()).availableAmount(po.getAvailableAmount())
                .accountStatus(po.getAccountStatus())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private UserCreditOrder toCreditOrderPO(AdminCreditOrderEntity e) {
        UserCreditOrder po = new UserCreditOrder();
        po.setId(e.getId()); po.setUserId(e.getUserId());
        po.setOrderId(e.getOrderId()); po.setTradeName(e.getTradeName());
        po.setTradeType(e.getTradeType()); po.setTradeAmount(e.getTradeAmount());
        po.setOutBusinessNo(e.getOutBusinessNo());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminCreditOrderEntity toOrderEntity(UserCreditOrder po) {
        if (po == null) return null;
        return AdminCreditOrderEntity.builder()
                .id(po.getId()).userId(po.getUserId()).orderId(po.getOrderId())
                .tradeName(po.getTradeName()).tradeType(po.getTradeType())
                .tradeAmount(po.getTradeAmount()).outBusinessNo(po.getOutBusinessNo())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
