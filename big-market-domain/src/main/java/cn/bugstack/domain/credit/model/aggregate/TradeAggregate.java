package cn.bugstack.domain.credit.model.aggregate;

import cn.bugstack.domain.credit.event.CreditAdjustSuccessMessage;
import cn.bugstack.domain.credit.model.entity.CreditAccountEntity;
import cn.bugstack.domain.credit.model.entity.CreditOrderEntity;
import cn.bugstack.domain.credit.model.valobj.TradeNameVO;
import cn.bugstack.domain.credit.model.valobj.TradeTypeVO;
import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.types.common.TaskStateVO;
import cn.bugstack.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

import static cn.bugstack.domain.activity.model.valobj.OrderStateVO.completed;

/**
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TradeAggregate {

    private String userId;
    /**
     * 积分账户实体
     */
    private CreditAccountEntity creditAccountEntity;
    /**
     * 积分订单实体
     */
    private CreditOrderEntity creditOrderEntity;

    private TaskEntity  taskEntity;


    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount) {
        return CreditAccountEntity.builder()
                .userId(userId)
                .adjustAmount(adjustAmount)
                .build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId, TradeNameVO  tradeNameVO,
                                                      TradeTypeVO tradeTypeVO, BigDecimal tradeAmount,
                                                      String outBusinessNo) {
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeNameVO)
                .tradeType(tradeTypeVO)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();
    }

    public static TaskEntity createTaskEntity(String userId, String topic, String id, BaseEvent.EventMessage<CreditAdjustSuccessMessage> eventMessage) {

        TaskEntity task = new TaskEntity();
        task.setUserId(userId);
        task.setTopic(topic);
        task.setMessageId(id);
        task.setMessage(JSON.toJSONString(eventMessage));
        task.setState(TaskStateVO.complete);
        return task;
    }
}
