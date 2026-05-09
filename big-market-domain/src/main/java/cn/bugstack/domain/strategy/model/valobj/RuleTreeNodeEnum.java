package cn.bugstack.domain.strategy.model.valobj;

import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.impl.RuleLockLogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.impl.RuleLuckAwardLogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.impl.RuleStockLogicTreeNode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RuleTreeNodeEnum {
    RULE_LOCK("rule_lock", "次数锁节点", RuleLockLogicTreeNode.class),
    RULE_LUCK_AWARD("rule_luck_award", "奖品兜底节点", RuleLuckAwardLogicTreeNode.class),
    RULE_STOCK("rule_stock", "库存扣减节点", RuleStockLogicTreeNode.class),
    ;
    public final String nodeName;
    public final String description;
    public final Class logicChain;

    public final static Map<String, RuleTreeNodeEnum> nodeEnumMap = Stream.of(RuleTreeNodeEnum.values()).
            collect(Collectors.toMap(RuleTreeNodeEnum::getNodeName, logicChainEnum -> logicChainEnum, (a, b) -> a));
    public final static Map<String, String> nodeNameMap = Stream.of(RuleTreeNodeEnum.values()).
            collect(Collectors.toMap(RuleTreeNodeEnum::getNodeName, logicChainEnum -> logicChainEnum.description, (a, b) -> a));

    RuleTreeNodeEnum(String nodeName, String description, Class<? extends ILogicTreeNode> logicChain) {
        this.nodeName = nodeName;
        this.description = description;
        this.logicChain = logicChain;
    }

    public Object getChain() {
        return this.logicChain;
    }


    public static String getLogicChainNameLowerByNodeName(String nodeName) {
        return nodeEnumMap.get(nodeName).getLogicChainNameLower();
    }


    public String getLogicChainNameLower() {
        return StringUtils.uncapitalize(getLogicChain().getSimpleName());
    }
}
