package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返利配置启停请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateConfigToggleRequestDTO {

    /** 自增ID */
    private Long id;

    /** 目标状态 */
    private String state;

}
