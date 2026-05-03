package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.RebateConfigPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.RebateConfigSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RebateConfigToggleRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RebateConfigResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

/**
 * 后台返利配置管理 API 接口
 */
public interface IAdminRebateService {

    /** 保存返利配置 */
    Response<Void> save(RebateConfigSaveRequestDTO request);

    /** 删除返利配置 */
    Response<Void> delete(Long id);

    /** 启停返利配置 */
    Response<Void> toggle(RebateConfigToggleRequestDTO request);

    /** 返利配置分页列表 */
    Response<PageResponseDTO<RebateConfigResponseDTO>> list(RebateConfigPageRequestDTO request);

}
