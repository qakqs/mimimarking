package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 规则树节点连线实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRuleTreeNodeLineEntity {

    private Long id;
    private String treeId;
    private String ruleNodeFrom;
    private String ruleNodeTo;
    private String ruleLimitType;
    private String ruleLimitValue;
    private Date createTime;
    private Date updateTime;

}
