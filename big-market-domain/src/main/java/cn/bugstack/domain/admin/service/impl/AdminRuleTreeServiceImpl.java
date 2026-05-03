package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.aggregate.AdminRuleTreeAggregate;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.domain.admin.repository.IAdminRuleTreeRepository;
import cn.bugstack.domain.admin.service.IAdminRuleTreeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminRuleTreeServiceImpl implements IAdminRuleTreeService {

    private final IAdminRuleTreeRepository adminRuleTreeRepository;

    public AdminRuleTreeServiceImpl(IAdminRuleTreeRepository adminRuleTreeRepository) {
        this.adminRuleTreeRepository = adminRuleTreeRepository;
    }

    @Override
    public void create(AdminRuleTreeEntity entity) {
        adminRuleTreeRepository.saveRuleTree(entity);
    }

    @Override
    public void update(AdminRuleTreeEntity entity) {
        adminRuleTreeRepository.updateRuleTree(entity);
    }

    @Override
    public void delete(String treeId) {
        adminRuleTreeRepository.deleteRuleTree(treeId);
    }

    @Override
    public AdminRuleTreeAggregate detail(String treeId) {
        AdminRuleTreeEntity tree = adminRuleTreeRepository.queryRuleTreeById(treeId);
        List<AdminRuleTreeNodeEntity> nodes = adminRuleTreeRepository.queryNodesByTreeId(treeId);
        List<AdminRuleTreeNodeLineEntity> lines = adminRuleTreeRepository.queryLinesByTreeId(treeId);
        return AdminRuleTreeAggregate.builder().tree(tree).nodes(nodes).lines(lines).build();
    }

    @Override
    public List<AdminRuleTreeEntity> list(int page, int pageSize, String treeName) {
        int offset = (page - 1) * pageSize;
        return adminRuleTreeRepository.queryRuleTreePage(offset, pageSize, treeName);
    }

    @Override
    public int count(String treeName) {
        return adminRuleTreeRepository.countRuleTree(treeName);
    }

    @Override
    public void saveNode(AdminRuleTreeNodeEntity entity) {
        adminRuleTreeRepository.saveRuleTreeNode(entity);
    }

    @Override
    public void deleteNode(Long id) {
        adminRuleTreeRepository.deleteRuleTreeNode(id);
    }

    @Override
    public void saveLine(AdminRuleTreeNodeLineEntity entity) {
        adminRuleTreeRepository.saveRuleTreeNodeLine(entity);
    }

    @Override
    public void deleteLine(Long id) {
        adminRuleTreeRepository.deleteRuleTreeNodeLine(id);
    }

}
