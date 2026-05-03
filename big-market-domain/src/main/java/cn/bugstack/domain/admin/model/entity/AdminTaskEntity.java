package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 任务实体（报表用）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminTaskEntity {

    private String id;
    private String userId;
    private String messageId;
    private String topic;
    private String message;
    private String state;
    private Date createTime;
    private Date updateTime;

}
