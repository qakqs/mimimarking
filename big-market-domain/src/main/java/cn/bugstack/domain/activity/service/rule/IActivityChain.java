package cn.bugstack.domain.activity.service.rule;

import cn.bugstack.types.model.ActionChainModel;

public interface IActivityChain extends IActivityChainArmory {

    Boolean action(ActionChainModel actionChainModel);
}
