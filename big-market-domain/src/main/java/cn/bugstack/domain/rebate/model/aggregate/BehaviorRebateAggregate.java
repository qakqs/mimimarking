package cn.bugstack.domain.rebate.model.aggregate;

import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateAggregate{

    /**
     * 用户id
     */
    private String userId;

    /**
     * 消息主体
     */
    private  BehaviorRebateOrderEntity behaviorRebateOrderEntity;

    private TaskEntity taskEntity;
}
