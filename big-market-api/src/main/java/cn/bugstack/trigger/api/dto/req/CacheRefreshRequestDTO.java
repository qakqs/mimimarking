package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存刷新请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheRefreshRequestDTO {

    /** 策略ID */
    private Long strategyId;

}
