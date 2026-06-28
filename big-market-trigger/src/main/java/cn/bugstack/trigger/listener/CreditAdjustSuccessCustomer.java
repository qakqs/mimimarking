package cn.bugstack.trigger.listener;

import cn.bugstack.domain.activity.model.entity.DeliveryOrderEntity;
import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.credit.event.CreditAdjustSuccessMessage;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CreditAdjustSuccessCustomer {
    private static final Log log = Log.get(CreditAdjustSuccessCustomer.class);

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.credit_adjust_success}"))
    public void listener(String message) {
        try {
            log.info("监听积分账户调整成功消息，进行交易商品发货 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();

            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
            deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());
            raffleActivityAccountQuotaService.updateSkuRechargeOrder(deliveryOrderEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }

}
