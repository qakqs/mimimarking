package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 用户中奖记录实体（报表用）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserAwardRecordEntity {

    private String id;
    private String userId;
    private Long activityId;
    private Long strategyId;
    private String orderId;
    private Integer awardId;
    private String awardTitle;
    private Date awardTime;
    private String awardState;
    private Date createTime;
    private Date updateTime;

}
