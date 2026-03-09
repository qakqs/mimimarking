package cn.bugstack.domain.activity.service.rule;

import cn.bugstack.domain.activity.service.rule.impl.AbstractActivityChain;
import cn.bugstack.domain.activity.service.rule.impl.ActivitySkuStockActionChain;
import cn.bugstack.domain.activity.service.rule.impl.BaseActivityChain;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Getter
public enum ActivityChainEnum {

    ACTIVITY_BASE_ACTION("activity_base_action", "默认责任链", BaseActivityChain.class),
    ACTIVITY_SKU_STOCK_ACTION("activity_sku_stock_action", "默认责任链", ActivitySkuStockActionChain.class),
    ;
    public final String chainName;
    public final String description;
    public final Class actionChain;

    public final static Map<String, ActivityChainEnum> chainEnumMap = Stream.of(ActivityChainEnum.values()).
            collect(Collectors.toMap(ActivityChainEnum::getChainName, logicChainEnum -> logicChainEnum, (a, b) -> a));

    ActivityChainEnum(String chainName, String description, Class<? extends AbstractActivityChain> actionChain) {
        this.chainName = chainName;
        this.description = description;
        this.actionChain = actionChain;
    }

    public String getChainName() {
        return this.chainName;
    }

    public Object getChain() {
        return this.actionChain;
    }


    public static String getLogicChainNameLowerByChainName(String chainName) {
        return chainEnumMap.get(chainName).getLogicChainNameLower();
    }


    public String getLogicChainNameLower() {
        return StringUtils.uncapitalize(getActionChain().getSimpleName());
    }

}
