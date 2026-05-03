package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动次数配置保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCountSaveRequestDTO {

    /** 活动次数配置ID（编辑时传入） */
    private Long activityCountId;

    /** 活动ID */
    private Long activityId;

    /** 总次数 */
    private Integer totalCount;

    /** 月次数 */
    private Integer monthCount;

    /** 日次数 */
    private Integer dayCount;

}
