package cn.bugstack.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /** 用户ID */
    private String userId;
    /** 用户名 */
    private String username;
    /** 密码（加密后） */
    private String password;
    /** 用户姓名 */
    private String name;
    /** 用户邮箱 */
    private String email;
    /** 用户手机号 */
    private String phone;
    /** 用户状态 */
    private String status;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
