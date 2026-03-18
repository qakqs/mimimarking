package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.event.SendAwardMessageEvent;
import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.service.distribute.IDistributeAward;
import cn.bugstack.types.common.TaskStateVO;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.event.SendAwardMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AwardService implements IAwardService {

    @Resource
    IAwardRepository awardRepository;

    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Resource
    private Map<String, IDistributeAward>  distributeAwardMap;


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessage sendAwardMessage = new SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);
        // 构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);

        // 构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .task(taskEntity)
                .userAwardRecord(userAwardRecordEntity)
                .build();

        // 存储聚合对象 - 一个事务下，用户的中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);


    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
       String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
       if (null == awardKey) {
           log.error("奖品分发奖品id不存在 awardId:{}",  distributeAwardEntity.getAwardId());
           return;
       }
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
       if (null == distributeAward) {
           log.error("分发奖品对应服务不存在 awardKey:{}", awardKey);
           return;
       }
       distributeAward.giveOutPrizes(distributeAwardEntity);
    }
}
