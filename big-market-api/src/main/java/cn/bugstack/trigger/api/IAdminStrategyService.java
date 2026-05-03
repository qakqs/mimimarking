package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.StrategyAwardSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyRuleSaveRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.StrategyDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyPageResponseDTO;

import java.util.List;

/**
 * 后台策略管理 API 接口
 */
public interface IAdminStrategyService {

    /** 创建策略 */
    Response<Void> create(StrategyCreateRequestDTO request);

    /** 更新策略 */
    Response<Void> update(StrategyCreateRequestDTO request);

    /** 删除策略 */
    Response<Void> delete(Long strategyId);

    /** 策略详情 */
    Response<StrategyDetailResponseDTO> detail(Long strategyId);

    /** 策略分页列表 */
    Response<PageResponseDTO<StrategyPageResponseDTO>> list(StrategyPageRequestDTO request);

    /** 策略奖品列表 */
    Response<List<StrategyAwardSaveRequestDTO>> awardList(Long strategyId);

    /** 保存策略奖品 */
    Response<Void> saveAward(StrategyAwardSaveRequestDTO request);

    /** 删除策略奖品 */
    Response<Void> deleteAward(Long id);

    /** 策略规则列表 */
    Response<List<StrategyRuleSaveRequestDTO>> ruleList(Long strategyId);

    /** 保存策略规则 */
    Response<Void> saveRule(StrategyRuleSaveRequestDTO request);

    /** 删除策略规则 */
    Response<Void> deleteRule(Long id);

}
