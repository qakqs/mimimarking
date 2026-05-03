package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返利配置响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateConfigResponseDTO {

    /** 自增ID */
    private Long id;

    /** 行为类型 */
    private String behaviorType;

    /** 返利描述 */
    private String rebateDesc;

    /** 返利类型 */
    private String rebateType;

    /** 返利配置 */
    private String rebateConfig;

    /** 状态 */
    private String state;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;

}
