package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务记录查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskPageRequestDTO extends PageRequestDTO {

    /** 用户ID */
    private String userId;

    /** 任务状态 */
    private String state;

    /** 消息Topic */
    private String topic;

}
