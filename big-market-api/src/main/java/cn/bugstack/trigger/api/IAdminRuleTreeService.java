package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.RuleTreeCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeLineSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreePageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.RuleTreeDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreePageResponseDTO;

/**
 * 后台规则树管理 API 接口
 */
public interface IAdminRuleTreeService {

    /** 创建规则树 */
    Response<Void> create(RuleTreeCreateRequestDTO request);

    /** 更新规则树 */
    Response<Void> update(RuleTreeCreateRequestDTO request);

    /** 删除规则树 */
    Response<Void> delete(String treeId);

    /** 规则树详情（含节点和连线） */
    Response<RuleTreeDetailResponseDTO> detail(String treeId);

    /** 规则树分页列表 */
    Response<PageResponseDTO<RuleTreePageResponseDTO>> list(RuleTreePageRequestDTO request);

    /** 保存节点 */
    Response<Void> saveNode(RuleTreeNodeSaveRequestDTO request);

    /** 删除节点 */
    Response<Void> deleteNode(Long id);

    /** 保存连线 */
    Response<Void> saveLine(RuleTreeNodeLineSaveRequestDTO request);

    /** 删除连线 */
    Response<Void> deleteLine(Long id);

}
