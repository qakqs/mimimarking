package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.RuleTreeCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeLineSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreePageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyAwardSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyRuleSaveRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.RuleTreeDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreePageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyPageResponseDTO;

import java.util.List;

/**
 * 后台策略管理 API 接口（含规则树管理）
 */
public interface IAdminStrategyService {


    Response<Void> update(StrategyCreateRequestDTO request);

    Response<Void> delete(Long strategyId);

    Response<StrategyDetailResponseDTO> detail(Long strategyId);

    Response<PageResponseDTO<StrategyPageResponseDTO>> list(StrategyPageRequestDTO request);

    // ====== Strategy Award ======

    Response<List<StrategyAwardSaveRequestDTO>> awardList(Long strategyId);

    Response<Void> saveAward(StrategyAwardSaveRequestDTO request);

    Response<Void> deleteAward(Long id);

    // ====== Strategy Rule ======

    Response<List<StrategyRuleSaveRequestDTO>> ruleList(Long strategyId);

    Response<Void> saveRule(StrategyRuleSaveRequestDTO request);

    Response<Void> deleteRule(Long id);

    // ====== Rule Tree ======

    Response<Void> createRuleTree(RuleTreeCreateRequestDTO request);

    Response<Void> updateRuleTree(RuleTreeCreateRequestDTO request);

    Response<Void> deleteRuleTree(String treeId);

    Response<RuleTreeDetailResponseDTO> detailRuleTree(String treeId);

    Response<PageResponseDTO<RuleTreePageResponseDTO>> listRuleTree(RuleTreePageRequestDTO request);

    Response<Void> saveRuleTreeNode(RuleTreeNodeSaveRequestDTO request);

    Response<Void> deleteRuleTreeNode(Long id);

    Response<Void> saveRuleTreeNodeLine(RuleTreeNodeLineSaveRequestDTO request);

    Response<Void> deleteRuleTreeNodeLine(Long id);

}
