package cn.bugstack.domain.strategy.service.annoation;

import cn.bugstack.domain.strategy.model.vo.LogicModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {

        LogicModel logicMode();

}
