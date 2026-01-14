package cn.bugstack.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {
    ALLOW("0000", "执行后续流程，不受规则引擎影响"),
    TAKE_OVER("0001", "执行后续流程，受规则引擎影响");


    private final String code;
    private final String info;
}
