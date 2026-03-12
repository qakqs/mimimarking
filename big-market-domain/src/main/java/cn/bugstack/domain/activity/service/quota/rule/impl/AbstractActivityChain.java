package cn.bugstack.domain.activity.service.quota.rule.impl;

import cn.bugstack.domain.activity.service.quota.rule.IActivityChain;

public abstract class AbstractActivityChain implements IActivityChain {

    private IActivityChain next;

    @Override
    public IActivityChain next() {
        return this.next;
    }

    @Override
    public IActivityChain appendNext(IActivityChain next) {
        this.next = next;
        return next;
    }

}
