package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户分页查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPageRequestDTO extends PageRequestDTO {

    /** 用户名（模糊搜索） */
    private String username;

    /** 手机号 */
    private String phone;

    /** 状态 */
    private String status;

}
