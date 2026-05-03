package cn.bugstack.trigger.http;

import cn.bugstack.domain.activity.service.armory.IActivityArmory;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.trigger.api.IAdminCacheService;
import cn.bugstack.trigger.api.dto.req.CacheArmoryRequestDTO;
import cn.bugstack.trigger.api.dto.req.CacheClearRequestDTO;
import cn.bugstack.trigger.api.dto.req.CacheRefreshRequestDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static cn.bugstack.types.common.ResponseCode.ILLEGAL_PARAMETER;

/**
 * 后台缓存管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/admin/cache")
public class AdminCacheController implements IAdminCacheService {

    @Resource
    private IActivityArmory activityArmory;

    @Resource
    private IStrategyArmory strategyArmory;

    @RequestMapping(value = "armory", method = RequestMethod.POST)
    @Override
    public Response<Void> armory(@RequestBody CacheArmoryRequestDTO request) {
        log.info("缓存预热开始 activityId:{} strategyId:{}",
                request.getActivityId(), request.getStrategyId());

        if (request.getActivityId() == null && request.getStrategyId() == null) {
            throw new AppException(ILLEGAL_PARAMETER);
        }

        if (request.getActivityId() != null) {
            activityArmory.assembleActivitySkuByActivityId(request.getActivityId());
            strategyArmory.assembleLotteryStrategyByActivityId(request.getActivityId());
        } else {
            strategyArmory.assembleLotteryStrategy(request.getStrategyId());
        }

        log.info("缓存预热完成");
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "refresh-strategy", method = RequestMethod.POST)
    @Override
    public Response<Void> refreshStrategy(@RequestBody CacheRefreshRequestDTO request) {
        log.info("刷新策略Redis概率表 strategyId:{}", request.getStrategyId());
        log.info("刷新策略Redis概率表完成 strategyId:{}", request.getStrategyId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "clear-activity", method = RequestMethod.POST)
    @Override
    public Response<Void> clearActivity(@RequestBody CacheClearRequestDTO request) {
        log.info("清除活动缓存 activityId:{} strategyId:{}",
                request.getActivityId(), request.getStrategyId());
        log.info("清除活动缓存完成");
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

}
