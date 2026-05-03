package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.CacheArmoryRequestDTO;
import cn.bugstack.trigger.api.dto.req.CacheClearRequestDTO;
import cn.bugstack.trigger.api.dto.req.CacheRefreshRequestDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

/**
 * 后台缓存管理 API 接口
 */
public interface IAdminCacheService {

    /** 活动/策略数据预热 */
    Response<Void> armory(CacheArmoryRequestDTO request);

    /** 刷新策略Redis概率表 */
    Response<Void> refreshStrategy(CacheRefreshRequestDTO request);

    /** 清除活动缓存 */
    Response<Void> clearActivity(CacheClearRequestDTO request);

}
