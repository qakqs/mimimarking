package cn.bugstack.domain.rebate.event;

import cn.bugstack.domain.activity.model.entity.SkuRechargeEntity;
import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.rebate.model.valobj.RebateTypeVO;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.event.RebateMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户返利行为消息处理
 */
@Slf4j
@Component
public class RebateMessageCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("用户返利消息，消费处理 topic：{}， message:{}", topic, message);
            BaseEvent.EventMessage<RebateMessage> rebateMessageEventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<RebateMessage>>() {
            });
            RebateMessage rebateMessage = rebateMessageEventMessage.getData();
            if (!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())) {
                log.info("用户返利消息，非sku奖励不处理 topic：{}， message:{}", topic, message);
                return;
            }
            // 入账奖励
            SkuRechargeEntity skuRechargeEntity = SkuRechargeEntity
                    .builder()
                    .userId(rebateMessage.getUserId())
                    .sku(Long.valueOf(rebateMessage.getRebateConfig()))
                    .outBusinessNo(rebateMessage.getBizId())
                    .build();
            // 2. 入账奖励
            raffleActivityAccountQuotaService.createSkuRechargeOrder(skuRechargeEntity);


        } catch (Exception e) {
            log.error("用户返利消息，消费失败 topic：{}， message:{}", topic, message, e);
        }

    }
}
