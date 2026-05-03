package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则树连线保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineSaveRequestDTO {

    /** 自增ID（编辑时传入） */
    private Long id;

    /** 规则树ID */
    private String treeId;

    /** 来源节点Key */
    private String ruleNodeFrom;

    /** 目标节点Key */
    private String ruleNodeTo;

    /** 限定类型：EQUAL/GE/LE/GT/LT */
    private String ruleLimitType;

    /** 限定值 */
    private String ruleLimitValue;

}
