package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户禁用/启用请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDisableRequestDTO {

    /** 用户ID */
    private String userId;

    /** 目标状态 */
    private String status;

}
