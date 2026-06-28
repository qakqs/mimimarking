package cn.bugstack.domain.award.event;

import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.service.IAwardService;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.event.SendAwardMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessage> {
    private static final Log log = Log.get(SendAwardMessageEvent.class);

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;




    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
