package cn.bugstack.domain.credit.event;

import cn.bugstack.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class CreditAdjustSuccessMessageEvent extends BaseEvent<CreditAdjustSuccessMessage> {


    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;
    @Override
    public EventMessage<CreditAdjustSuccessMessage> buildEventMessage(CreditAdjustSuccessMessage data) {
        return EventMessage.<CreditAdjustSuccessMessage>builder()
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
