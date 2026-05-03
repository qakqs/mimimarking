package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户分页列表项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPageResponseDTO {

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 用户姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 状态 */
    private String status;

    /** 创建时间 */
    private String createTime;

}
