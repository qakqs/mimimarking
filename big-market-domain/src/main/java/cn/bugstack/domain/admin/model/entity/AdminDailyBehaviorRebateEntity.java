package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 每日行为返利配置实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDailyBehaviorRebateEntity {

    private Long id;
    private String behaviorType;
    private String rebateDesc;
    private String rebateType;
    private String rebateConfig;
    private String state;
    private Date createTime;
    private Date updateTime;

}
