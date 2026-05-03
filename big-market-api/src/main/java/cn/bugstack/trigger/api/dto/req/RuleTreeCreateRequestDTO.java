package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建/编辑规则树请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeCreateRequestDTO {

    /** 规则树ID（编辑时传入） */
    private String treeId;

    /** 规则树名称 */
    private String treeName;

    /** 规则树描述 */
    private String treeDesc;

    /** 规则根节点Key */
    private String treeRootRuleKey;

}
