package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 用户 PO（持久化对象）
 */
@Data
public class User {

    /** 自增ID */
    private Long id;
    /** 用户ID */
    private String userId;
    /** 用户名 */
    private String username;
    /** 密码 */
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
