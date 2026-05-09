package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 规则树节点响应（匹配 RuleTreeNodeVO，节点内嵌出边）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeResponseDTO {

    private String treeId;
    private String ruleKey;
    private String ruleDesc;
    private String ruleValue;

    /** 该节点的所有出边 */
    private List<RuleTreeNodeLineResponseDTO> treeNodeLineVOList;

}
