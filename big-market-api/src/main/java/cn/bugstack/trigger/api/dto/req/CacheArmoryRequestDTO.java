package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存预热请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheArmoryRequestDTO {

    /** 活动ID */
    private Long activityId;

    /** 策略ID */
    private Long strategyId;

}
