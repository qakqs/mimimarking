package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.ActivityCountSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.ActivityCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.ActivityPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.ActivitySkuSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.ActivitySkuStockRequestDTO;
import cn.bugstack.trigger.api.dto.req.ActivityToggleRequestDTO;
import cn.bugstack.trigger.api.dto.resp.ActivityDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.ActivityPageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.ActivitySkuResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

import java.util.List;

/**
 * 后台活动管理 API 接口
 */
public interface IAdminActivityService {


    /** 更新活动 */
    Response<Void> update(ActivityCreateRequestDTO request);

    /** 删除活动 */
    Response<Void> delete(Long activityId);

    /** 活动详情 */
    Response<ActivityDetailResponseDTO> detail(Long activityId);

    /** 活动分页列表 */
    Response<PageResponseDTO<ActivityPageResponseDTO>> list(ActivityPageRequestDTO request);

    /** 活动上下架 */
    Response<Void> toggleStatus(ActivityToggleRequestDTO request);

    /** 保存活动次数配置 */
    Response<Void> saveCount(ActivityCountSaveRequestDTO request);

    /** 查询活动次数配置 */
    Response<ActivityCountSaveRequestDTO> getCount(Long activityId);

    /** 保存SKU */
    Response<Void> saveSku(ActivitySkuSaveRequestDTO request);

    /** 删除SKU */
    Response<Void> deleteSku(Long sku);

    /** 活动SKU列表 */
    Response<List<ActivitySkuResponseDTO>> skuList(Long activityId);

    /** 调整SKU库存 */
    Response<Void> adjustSkuStock(ActivitySkuStockRequestDTO request);

}
