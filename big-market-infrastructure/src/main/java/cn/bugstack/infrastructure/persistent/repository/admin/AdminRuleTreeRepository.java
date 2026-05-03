package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.domain.admin.repository.IAdminRuleTreeRepository;
import cn.bugstack.infrastructure.persistent.dao.IRuleTreeDao;
import cn.bugstack.infrastructure.persistent.dao.IRuleTreeNodeDao;
import cn.bugstack.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import cn.bugstack.infrastructure.persistent.po.RuleTree;
import cn.bugstack.infrastructure.persistent.po.RuleTreeNode;
import cn.bugstack.infrastructure.persistent.po.RuleTreeNodeLine;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 规则树仓储实现
 */
@Repository
public class AdminRuleTreeRepository implements IAdminRuleTreeRepository {

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public void saveRuleTree(AdminRuleTreeEntity entity) {
        ruleTreeDao.insert(toRuleTreePO(entity));
    }

    @Override
    public void updateRuleTree(AdminRuleTreeEntity entity) {
        ruleTreeDao.update(toRuleTreePO(entity));
    }

    @Override
    public void deleteRuleTree(String treeId) {
        ruleTreeDao.deleteByTreeId(treeId);
        ruleTreeNodeDao.deleteByTreeId(treeId);
        ruleTreeNodeLineDao.deleteByTreeId(treeId);
    }

    @Override
    public AdminRuleTreeEntity queryRuleTreeById(String treeId) {
        RuleTree po = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        return toRuleTreeEntity(po);
    }

    @Override
    public List<AdminRuleTreeEntity> queryRuleTreePage(int offset, int limit, String treeName) {
        List<RuleTree> list = ruleTreeDao.queryRuleTreePage(offset, limit, treeName);
        return list.stream().map(this::toRuleTreeEntity).collect(Collectors.toList());
    }

    @Override
    public int countRuleTree(String treeName) {
        return ruleTreeDao.countRuleTree(treeName);
    }

    @Override
    public void saveRuleTreeNode(AdminRuleTreeNodeEntity entity) {
        ruleTreeNodeDao.insert(toRuleTreeNodePO(entity));
    }

    @Override
    public void deleteRuleTreeNode(Long id) {
        ruleTreeNodeDao.deleteById(id);
    }

    @Override
    public List<AdminRuleTreeNodeEntity> queryNodesByTreeId(String treeId) {
        List<RuleTreeNode> list = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        return list.stream().map(this::toRuleTreeNodeEntity).collect(Collectors.toList());
    }

    @Override
    public void saveRuleTreeNodeLine(AdminRuleTreeNodeLineEntity entity) {
        ruleTreeNodeLineDao.insert(toRuleTreeNodeLinePO(entity));
    }

    @Override
    public void deleteRuleTreeNodeLine(Long id) {
        ruleTreeNodeLineDao.deleteById(id);
    }

    @Override
    public List<AdminRuleTreeNodeLineEntity> queryLinesByTreeId(String treeId) {
        List<RuleTreeNodeLine> list = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
        return list.stream().map(this::toRuleTreeNodeLineEntity).collect(Collectors.toList());
    }

    // ===== PO <-> Entity mapping =====

    private RuleTree toRuleTreePO(AdminRuleTreeEntity e) {
        RuleTree po = new RuleTree();
        po.setId(e.getId()); po.setTreeId(e.getTreeId());
        po.setTreeName(e.getTreeName()); po.setTreeDesc(e.getTreeDesc());
        po.setTreeRootRuleKey(e.getTreeRootRuleKey());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminRuleTreeEntity toRuleTreeEntity(RuleTree po) {
        if (po == null) return null;
        return AdminRuleTreeEntity.builder()
                .id(po.getId()).treeId(po.getTreeId()).treeName(po.getTreeName())
                .treeDesc(po.getTreeDesc()).treeRootRuleKey(po.getTreeRootRuleKey())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private RuleTreeNode toRuleTreeNodePO(AdminRuleTreeNodeEntity e) {
        RuleTreeNode po = new RuleTreeNode();
        po.setId(e.getId()); po.setTreeId(e.getTreeId());
        po.setRuleKey(e.getRuleKey()); po.setRuleDesc(e.getRuleDesc());
        po.setRuleValue(e.getRuleValue());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminRuleTreeNodeEntity toRuleTreeNodeEntity(RuleTreeNode po) {
        if (po == null) return null;
        return AdminRuleTreeNodeEntity.builder()
                .id(po.getId()).treeId(po.getTreeId()).ruleKey(po.getRuleKey())
                .ruleDesc(po.getRuleDesc()).ruleValue(po.getRuleValue())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private RuleTreeNodeLine toRuleTreeNodeLinePO(AdminRuleTreeNodeLineEntity e) {
        RuleTreeNodeLine po = new RuleTreeNodeLine();
        po.setId(e.getId()); po.setTreeId(e.getTreeId());
        po.setRuleNodeFrom(e.getRuleNodeFrom()); po.setRuleNodeTo(e.getRuleNodeTo());
        po.setRuleLimitType(e.getRuleLimitType()); po.setRuleLimitValue(e.getRuleLimitValue());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminRuleTreeNodeLineEntity toRuleTreeNodeLineEntity(RuleTreeNodeLine po) {
        if (po == null) return null;
        return AdminRuleTreeNodeLineEntity.builder()
                .id(po.getId()).treeId(po.getTreeId())
                .ruleNodeFrom(po.getRuleNodeFrom()).ruleNodeTo(po.getRuleNodeTo())
                .ruleLimitType(po.getRuleLimitType()).ruleLimitValue(po.getRuleLimitValue())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
