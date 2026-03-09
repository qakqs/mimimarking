package cn.bugstack.domain.activity.service.rule;

public interface IActivityChainArmory {

    IActivityChain next();

    IActivityChain appendNext(IActivityChain next);
}
