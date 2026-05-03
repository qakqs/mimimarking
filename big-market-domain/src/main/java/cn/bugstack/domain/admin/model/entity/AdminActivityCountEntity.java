package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 活动次数配置实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminActivityCountEntity {

    private Long id;
    private Long activityCountId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private Date createTime;
    private Date updateTime;

}
