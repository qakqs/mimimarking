package cn.bugstack.domain.admin.model.aggregate;

import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理 - 积分调额聚合（账户 + 流水）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreditAdjustAggregate {

    private AdminCreditAccountEntity account;
    private AdminCreditOrderEntity order;

}
