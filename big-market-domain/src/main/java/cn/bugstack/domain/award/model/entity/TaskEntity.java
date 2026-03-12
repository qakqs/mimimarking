package cn.bugstack.domain.award.model.entity;

import cn.bugstack.domain.award.model.valobj.TaskStateVO;
import cn.bugstack.types.event.BaseEvent;
import cn.bugstack.types.event.SendAwardMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TaskEntity {

    /**
     * 用户id
     */
    private String userId;

    private String messageId;

    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息主体
     */
    private BaseEvent.EventMessage<SendAwardMessage> message;
    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private TaskStateVO state;

}
