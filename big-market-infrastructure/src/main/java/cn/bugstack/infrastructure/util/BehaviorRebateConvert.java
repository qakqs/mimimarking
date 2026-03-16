package cn.bugstack.infrastructure.util;

import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.bugstack.infrastructure.persistent.po.DailyBehaviorRebate;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserBehaviorRebateOrder;
import cn.bugstack.types.common.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BehaviorRebateConvert {
    UserBehaviorRebateOrder convertBehaviorRebateOrder(BehaviorRebateOrderEntity entity);

    Task convertTask(TaskEntity taskEntity);

    List<DailyBehaviorRebateVO> convertdailyBehaviorRebateList(List<DailyBehaviorRebate> dailyBehaviorRebateList);

}
