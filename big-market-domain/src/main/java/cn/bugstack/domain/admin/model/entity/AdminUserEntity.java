package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserEntity {

    private Long id;
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String status;
    private Date createTime;
    private Date updateTime;

}
