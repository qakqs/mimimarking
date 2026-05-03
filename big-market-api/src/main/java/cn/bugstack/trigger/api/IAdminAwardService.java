package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.AwardCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.AwardPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.AwardDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.AwardPageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

/**
 * 后台奖品管理 API 接口
 */
public interface IAdminAwardService {

    /** 创建奖品 */
    Response<Void> create(AwardCreateRequestDTO request);

    /** 更新奖品 */
    Response<Void> update(AwardCreateRequestDTO request);

    /** 删除奖品 */
    Response<Void> delete(Integer awardId);

    /** 奖品详情 */
    Response<AwardDetailResponseDTO> detail(Integer awardId);

    /** 奖品分页列表 */
    Response<PageResponseDTO<AwardPageResponseDTO>> list(AwardPageRequestDTO request);

}
