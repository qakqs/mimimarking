package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务记录项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    /** 任务ID */
    private String id;

    /** 用户ID */
    private String userId;

    /** 消息Topic */
    private String topic;

    /** 消息ID */
    private String messageId;

    /** 任务状态 */
    private String state;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;

}
