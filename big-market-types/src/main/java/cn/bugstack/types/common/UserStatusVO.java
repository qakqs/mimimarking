package cn.bugstack.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusVO {

    ACTIVE(0, "正常"),
    DISABLED(1, "已禁用");

    private final Integer code;
    private final String desc;

}
