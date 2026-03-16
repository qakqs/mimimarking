package cn.bugstack.domain.rebate.service.impl;

import cn.bugstack.domain.rebate.event.SendRebateMessageEvent;
import cn.bugstack.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.bugstack.domain.rebate.model.entity.BehaviorEntity;
import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.bugstack.domain.rebate.repository.IBehaviorRebateRepository;
import cn.bugstack.domain.rebate.service.IBehaviorRebateService;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.types.common.TaskStateVO;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.event.RebateMessage;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        // 查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateList = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());

        if (CollectionUtils.isEmpty(dailyBehaviorRebateList)) {
            return new ArrayList<>();
        }
        //构建聚合对象
        List<String> orderList = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateList) {
            String bizId = this.buildBizId(behaviorEntity, dailyBehaviorRebateVO);
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity
                    .builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();
            orderList.add(behaviorRebateOrderEntity.getOrderId());

            // MQ消息
            RebateMessage rebateMessage = RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

            //构建事件消息
            BaseEvent.EventMessage<RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            //组装任务对象
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(JSON.toJSONString(rebateMessageEventMessage));
            taskEntity.setState(TaskStateVO.create);

            // 构建聚合对象
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate
                    .builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .build();
            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        // 存储聚合对象
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);


        //返回订单集合
        return orderList;
    }


    private String buildBizId(BehaviorEntity behaviorEntity, DailyBehaviorRebateVO dailyBehaviorRebateVO) {
        return behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
    }
}
