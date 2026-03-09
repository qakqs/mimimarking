package cn.bugstack.domain.activity.service.rule.impl;

import cn.bugstack.types.model.ActionChainModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class BaseActivityChain extends AbstractActivityChain{
    @Override
    public Boolean action(ActionChainModel actionChainModel) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        return next().action(actionChainModel);
    }
}
