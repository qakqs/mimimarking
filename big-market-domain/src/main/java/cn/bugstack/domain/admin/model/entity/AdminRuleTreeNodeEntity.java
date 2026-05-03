package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 规则树节点实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRuleTreeNodeEntity {

    private Long id;
    private String treeId;
    private String ruleKey;
    private String ruleDesc;
    private String ruleValue;
    private Date createTime;
    private Date updateTime;

}
