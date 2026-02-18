package cn.bugstack.domain.strategy.model.vo;

import cn.bugstack.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.bugstack.types.enums.ResponseCode.ILLEGAL_PARAMETER;

@Getter
@AllArgsConstructor
public enum LogicModel {
    RULE_WIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY", "before"),
    RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回", "before"),
    RULE_LOCK("rule_lock", "【抽奖前规则】抽奖n次后可解锁抽奖", "center"),
    RULE_LOCK_AWARD("rule_lock_award", "【抽奖前规则】幸运兜底", "after"),
    ;

    private final String code;
    private final String info;
    private final String type;
    private static final Map<String, LogicModel> logicModelMap = Stream.of(LogicModel.values()).
            collect(Collectors.toMap(LogicModel::getCode, logicModel -> logicModel, (a, b) -> a));

    public static boolean isCenter(String code) {
        LogicModel logicModel = logicModelMap.get(code);
        if (logicModel == null) {
            throw new AppException(ILLEGAL_PARAMETER);
        }
        return logicModel.getType().equals("center");
    }

}

