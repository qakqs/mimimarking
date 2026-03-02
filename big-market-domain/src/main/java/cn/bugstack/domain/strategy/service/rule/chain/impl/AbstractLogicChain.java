package cn.bugstack.domain.strategy.service.rule.chain.impl;

import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;

/**
 *
 */
public abstract class AbstractLogicChain implements ILogicChain {


    private ILogicChain nextChain;


    public ILogicChain next(){
        return this.nextChain;
    }

    public ILogicChain appendNext(ILogicChain next){
        this.nextChain = next;
        return next;
    }

    protected abstract String ruleModel();
}
