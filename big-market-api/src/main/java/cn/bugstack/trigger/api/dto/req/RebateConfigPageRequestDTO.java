package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 返利配置分页查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RebateConfigPageRequestDTO extends PageRequestDTO {

    /** 行为类型 */
    private String behaviorType;

    /** 状态 */
    private String state;

}
