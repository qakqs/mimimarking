package cn.bugstack.domain.admin.model.aggregate;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 后台管理 - 规则树聚合（树 + 节点列表 + 连线列表）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRuleTreeAggregate {

    private AdminRuleTreeEntity tree;
    private List<AdminRuleTreeNodeEntity> nodes;
    private List<AdminRuleTreeNodeLineEntity> lines;

}
