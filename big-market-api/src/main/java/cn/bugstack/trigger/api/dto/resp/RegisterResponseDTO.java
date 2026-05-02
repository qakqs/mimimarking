package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {

    /** 用户ID */
    private String userId;
    /** 用户名 */
    private String username;

}
