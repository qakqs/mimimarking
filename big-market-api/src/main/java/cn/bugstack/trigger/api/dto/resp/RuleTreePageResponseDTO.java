package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则树分页列表项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreePageResponseDTO {

    /** 规则树ID */
    private String treeId;

    /** 规则树名称 */
    private String treeName;

    /** 规则树描述 */
    private String treeDesc;

    /** 创建时间 */
    private String createTime;

}
