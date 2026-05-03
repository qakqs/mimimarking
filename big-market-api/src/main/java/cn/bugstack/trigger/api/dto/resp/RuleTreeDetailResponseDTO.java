package cn.bugstack.trigger.api.dto.resp;

import cn.bugstack.trigger.api.dto.req.RuleTreeNodeLineSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeSaveRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 规则树详情响应 DTO（含节点和连线完整结构）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeDetailResponseDTO {

    /** 规则树ID */
    private String treeId;

    /** 规则树名称 */
    private String treeName;

    /** 规则树描述 */
    private String treeDesc;

    /** 规则根节点Key */
    private String treeRootRuleKey;

    /** 节点列表 */
    private List<RuleTreeNodeSaveRequestDTO> nodes;

    /** 连线列表 */
    private List<RuleTreeNodeLineSaveRequestDTO> lines;

}
