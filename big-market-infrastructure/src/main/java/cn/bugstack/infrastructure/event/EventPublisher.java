package cn.bugstack.infrastructure.event;

import cn.bugstack.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventPublisher {

    @Resource
    RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            String mesJson = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic, mesJson);
            log.info("发送MQ消息 topic:{} message:{}", topic, mesJson);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }

    }

        public void publish(String topic, String eventMessage) {
        try {
            rabbitTemplate.convertAndSend(topic, eventMessage);
            log.info("发送MQ消息 topic:{} message:{}", topic, eventMessage);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }

    }

}
