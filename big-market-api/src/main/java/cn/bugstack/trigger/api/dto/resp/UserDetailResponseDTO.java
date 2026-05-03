package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详情响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponseDTO {

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 用户姓名 */
    private String name;

    /** 用户邮箱 */
    private String email;

    /** 用户手机号 */
    private String phone;

    /** 用户状态 */
    private String status;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;

}
