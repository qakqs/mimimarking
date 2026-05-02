package cn.bugstack.domain.user.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户状态值对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum UserStatusVO {

    ACTIVE("active", "活跃"),
    INACTIVE("inactive", "非活跃"),
    BANNED("banned", "封禁"),
    ;

    private String code;
    private String info;

}
