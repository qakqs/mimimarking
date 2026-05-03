package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则树节点保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeSaveRequestDTO {

    /** 自增ID（编辑时传入） */
    private Long id;

    /** 规则树ID */
    private String treeId;

    /** 规则Key */
    private String ruleKey;

    /** 规则描述 */
    private String ruleDesc;

    /** 规则值 */
    private String ruleValue;

}
