package cn.bugstack.infrastructure.event;

import cn.bugstack.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import cn.bugstack.types.common.Log;
import cn.bugstack.types.common.TraceContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private static final Log log = Log.get(EventPublisher.class);

    @Autowired(required = false)
    RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        String mesJson = JSON.toJSONString(eventMessage);
        try {
            if (rabbitTemplate == null) {
                log.warn("RabbitMQ 未启用，跳过消息发送 topic:{}", topic);
                return;
            }
            rabbitTemplate.convertAndSend(topic, (Object) mesJson, TRACE_ID_STAMPER);
            log.info("发送MQ消息 topic:{} message:{}", topic, mesJson);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, mesJson, e);
            throw e;
        }

    }

        public void publish(String topic, String eventMessage) {
        try {
            if (rabbitTemplate == null) {
                log.warn("RabbitMQ 未启用，跳过消息发送 topic:{}", topic);
                return;
            }
            rabbitTemplate.convertAndSend(topic, (Object) eventMessage, TRACE_ID_STAMPER);
            log.info("发送MQ消息 topic:{} message:{}", topic, eventMessage);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, eventMessage, e);
            throw e;
        }

    }

    /**
     * Stamps the current request's trace-id onto the AMQP message header so listeners can
     * rebind it into their MDC and keep the trace continuous across the MQ boundary.
     */
    private static final MessagePostProcessor TRACE_ID_STAMPER = new MessagePostProcessor() {
        @Override
        public Message postProcessMessage(Message message) {
            String traceId = TraceContext.currentTraceId();
            if (traceId != null) {
                message.getMessageProperties().setHeader(TraceContext.TRACE_ID_HEADER, traceId);
            }
            return message;
        }
};
}
