package cn.bugstack.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityStateVO {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    TERMINATED(2, "已终止");

    private final Integer code;
    private final String desc;

}
