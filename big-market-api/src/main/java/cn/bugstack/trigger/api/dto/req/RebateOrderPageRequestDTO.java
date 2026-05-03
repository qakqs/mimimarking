package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 返利订单查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RebateOrderPageRequestDTO extends PageRequestDTO {

    /** 用户ID */
    private String userId;

    /** 返利类型 */
    private String rebateType;

}
