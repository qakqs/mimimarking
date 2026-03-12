package cn.bugstack.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description 任务表，发送MQ
 * @create 2024-04-03 15:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    /** 自增ID */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    private String messageId;

    /** 消息主题 */
    private String topic;
    /** 消息主体 */
    private String message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
