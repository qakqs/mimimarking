package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.aggregate.AdminCreditAdjustAggregate;
import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;
import cn.bugstack.domain.admin.repository.IAdminCreditRepository;
import cn.bugstack.domain.admin.service.IAdminCreditService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminCreditServiceImpl implements IAdminCreditService {

    private final IAdminCreditRepository adminCreditRepository;

    public AdminCreditServiceImpl(IAdminCreditRepository adminCreditRepository) {
        this.adminCreditRepository = adminCreditRepository;
    }

    @Override
    public List<AdminCreditAccountEntity> accountList(int page, int pageSize, String userId) {
        int offset = (page - 1) * pageSize;
        return adminCreditRepository.queryCreditAccountPage(offset, pageSize, userId);
    }

    @Override
    public int accountCount(String userId) {
        return adminCreditRepository.countCreditAccount(userId);
    }

    @Override
    public AdminCreditAccountEntity accountDetail(String userId) {
        return adminCreditRepository.queryCreditAccountByUserId(userId);
    }

    @Override
    public void adjust(AdminCreditAdjustAggregate aggregate) {
        adminCreditRepository.updateCreditAccount(aggregate.getAccount());
        adminCreditRepository.insertCreditOrder(aggregate.getOrder());
    }

    @Override
    public List<AdminCreditOrderEntity> orderList(int page, int pageSize, String userId) {
        int offset = (page - 1) * pageSize;
        return adminCreditRepository.queryCreditOrderPage(offset, pageSize, userId);
    }

    @Override
    public int orderCount(String userId) {
        return adminCreditRepository.countCreditOrder(userId);
    }

}
