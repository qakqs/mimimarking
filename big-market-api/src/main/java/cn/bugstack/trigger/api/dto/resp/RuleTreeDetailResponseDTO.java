package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 规则树详情响应 DTO（匹配 RuleTreeVO — 节点 Map，每个节点内嵌出边）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeDetailResponseDTO {

    private String treeId;
    private String treeName;
    private String treeDesc;
    private String treeRootRuleNode;
    private Map<String, RuleTreeNodeResponseDTO> treeNodeMap;

}
