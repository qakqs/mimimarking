package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登出请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequestDTO {

    /** Token */
    private String token;

}
