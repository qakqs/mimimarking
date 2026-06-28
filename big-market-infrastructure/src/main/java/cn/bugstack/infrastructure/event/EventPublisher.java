package cn.bugstack.infrastructure.event;

import cn.bugstack.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import cn.bugstack.types.common.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private static final Log log = Log.get(EventPublisher.class);

    @Autowired(required = false)
    RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            if (rabbitTemplate == null) {
                log.warn("RabbitMQ 未启用，跳过消息发送 topic:{}", topic);
                return;
            }
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
            if (rabbitTemplate == null) {
                log.warn("RabbitMQ 未启用，跳过消息发送 topic:{}", topic);
                return;
            }
            rabbitTemplate.convertAndSend(topic, eventMessage);
            log.info("发送MQ消息 topic:{} message:{}", topic, eventMessage);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }

    }

}
