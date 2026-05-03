package cn.bugstack.domain.admin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台管理 - 规则树实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRuleTreeEntity {

    private Long id;
    private String treeId;
    private String treeName;
    private String treeDesc;
    private String treeRootRuleKey;
    private Date createTime;
    private Date updateTime;

}
