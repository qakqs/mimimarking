package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 中奖记录查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AwardRecordPageRequestDTO extends PageRequestDTO {

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;

    /** 奖品状态 */
    private String awardState;

}
