package cn.bugstack.domain.activity.service.rule.factory;

import cn.bugstack.domain.activity.service.rule.ActivityChainEnum;
import cn.bugstack.domain.activity.service.rule.IActivityChain;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultActivityChainFactory {
    private final IActivityChain activityChain;

    /**
     * 1. 通过构造函数注入。
     * 2. Spring 可以自动注入 IActionChain 接口实现类到 map 对象中，key 就是 bean 的名字。
     * 3. 活动下单动作的责任链是固定的，所以直接在构造函数中组装即可。
     */
    public DefaultActivityChainFactory(Map<String, IActivityChain> actionChainGroup) {
        activityChain = actionChainGroup.get(ActivityChainEnum.ACTIVITY_BASE_ACTION.getLogicChainNameLower());
        activityChain.appendNext(actionChainGroup.get(ActivityChainEnum.ACTIVITY_SKU_STOCK_ACTION.getLogicChainNameLower()));
    }


    public IActivityChain openActionChain() {
        return this.activityChain;
    }
}
