package cn.bugstack.domain.activity.service.quota.rule.impl;

import cn.bugstack.domain.activity.model.valobj.ActivityStateVO;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.domain.strategy.model.entity.ActionChainModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class BaseActivityChain extends AbstractActivityChain {
    @Override
    public Boolean action(ActionChainModel actionChainModel) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");
        if (!ActivityStateVO.open.equals(actionChainModel.getActivityEntity().getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR);
        }
        Date date = new Date();
        if (actionChainModel.getActivityEntity().getBeginDateTime().after(date) ||
                actionChainModel.getActivityEntity().getEndDateTime().before(date)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR);

        }
        return next().action(actionChainModel);
    }
}
