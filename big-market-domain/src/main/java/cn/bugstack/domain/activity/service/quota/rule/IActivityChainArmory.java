package cn.bugstack.domain.activity.service.quota.rule;

public interface IActivityChainArmory {

    IActivityChain next();

    IActivityChain appendNext(IActivityChain next);
}
