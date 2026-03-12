package cn.bugstack.domain.activity.service.quota.rule;

import cn.bugstack.domain.strategy.model.entity.ActionChainModel;

public interface IActivityChain extends IActivityChainArmory {

    Boolean action(ActionChainModel actionChainModel);
}
