package cn.bugstack.domain.strategy.service.rule.chain;


import cn.bugstack.domain.strategy.service.rule.chain.impl.AbstractLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.BackListLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.DefaultLogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum LogicChainEnum {
    RULE_BLACKLIST("rule_blacklist", "黑名单责任链", BackListLogicChain.class),
    RULE_WEIGHT("rule_weight", "权重责任链", RuleWeightLogicChain.class),
    DEFAULT("default", "默认责任链", DefaultLogicChain.class),
    ;
    public final String chainName;
    public final String description;
    public final Class logicChain;

    public final static Map<String, LogicChainEnum> chainEnumMap = Stream.of(LogicChainEnum.values()).
            collect(Collectors.toMap(LogicChainEnum::getChainName, logicChainEnum -> logicChainEnum, (a, b) -> a));

    LogicChainEnum(String chainName, String description, Class<? extends AbstractLogicChain> logicChain) {
        this.chainName = chainName;
        this.description = description;
        this.logicChain = logicChain;
    }

    public Object getChain() {
        return this.logicChain;
    }


    public static String getLogicChainNameLowerByChainName(String chainName) {
        return chainEnumMap.get(chainName).getLogicChainNameLower();
    }


    public String getLogicChainNameLower() {
        return StringUtils.uncapitalize(getLogicChain().getSimpleName());
    }
}
