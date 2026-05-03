package cn.bugstack.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AwardStateVO {

    CREATE("create", "创建"),
    COMPLETE("complete", "发奖完成"),
    FAIL("fail", "发奖失败");

    private final String code;
    private final String desc;

}
