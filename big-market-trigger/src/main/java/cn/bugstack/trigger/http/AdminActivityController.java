package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.aggregate.AdminActivityAggregate;
import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivitySkuEntity;
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
import cn.bugstack.types.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/activity")
public class AdminActivityController implements cn.bugstack.trigger.api.IAdminActivityService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminActivityService adminActivityService;

    public AdminActivityController(cn.bugstack.domain.admin.service.IAdminActivityService adminActivityService) {
        this.adminActivityService = adminActivityService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public Response<Void> create(@RequestBody ActivityCreateRequestDTO request) {
        log.info("创建活动开始 activityName:{}", request.getActivityName());
        AdminActivityEntity activity = toActivityEntity(request);
        AdminActivityAggregate aggregate = AdminActivityAggregate.builder().activity(activity).build();
        adminActivityService.create(aggregate);
        log.info("创建活动完成 activityName:{}", request.getActivityName());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @Override
    public Response<Void> update(@RequestBody ActivityCreateRequestDTO request) {
        log.info("更新活动开始 activityId:{}", request.getActivityId());
        adminActivityService.update(toActivityEntity(request));
        log.info("更新活动完成 activityId:{}", request.getActivityId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Override
    public Response<Void> delete(@RequestParam Long activityId) {
        log.info("删除活动开始 activityId:{}", activityId);
        adminActivityService.delete(activityId);
        log.info("删除活动完成 activityId:{}", activityId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @Override
    public Response<ActivityDetailResponseDTO> detail(@RequestParam Long activityId) {
        log.info("查询活动详情 activityId:{}", activityId);
        AdminActivityAggregate aggregate = adminActivityService.detail(activityId);
        ActivityDetailResponseDTO data = toDetailDTO(aggregate.getActivity());
        log.info("查询活动详情完成 activityId:{}", activityId);
        return Response.<ActivityDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<ActivityPageResponseDTO>> list(@RequestBody ActivityPageRequestDTO request) {
        log.info("查询活动分页列表 request:{}", request);
        List<AdminActivityEntity> entities = adminActivityService.list(
                request.getPageNum(), request.getPageSize(), request.getActivityName(), request.getState());
        int total = adminActivityService.count(request.getActivityName(), request.getState());
        List<ActivityPageResponseDTO> list = entities.stream()
                .map(this::toPageDTO).collect(Collectors.toList());
        log.info("查询活动分页列表完成 pageNum:{}", request.getPageNum());
        return Response.<PageResponseDTO<ActivityPageResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<ActivityPageResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "toggle-status", method = RequestMethod.POST)
    @Override
    public Response<Void> toggleStatus(@RequestBody ActivityToggleRequestDTO request) {
        log.info("活动上下架 activityId:{} state:{}", request.getActivityId(), request.getState());
        adminActivityService.toggleStatus(request.getActivityId(), Integer.parseInt(request.getState()));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "count/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveCount(@RequestBody ActivityCountSaveRequestDTO request) {
        log.info("保存活动次数配置 activityId:{}", request.getActivityId());
        AdminActivityCountEntity entity = AdminActivityCountEntity.builder()
                .activityCountId(request.getActivityCountId())
                .totalCount(request.getTotalCount())
                .dayCount(request.getDayCount())
                .monthCount(request.getMonthCount())
                .build();
        adminActivityService.saveCount(entity);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "count/detail", method = RequestMethod.GET)
    @Override
    public Response<ActivityCountSaveRequestDTO> getCount(@RequestParam Long activityId) {
        log.info("查询活动次数配置 activityId:{}", activityId);
        AdminActivityCountEntity entity = adminActivityService.getCount(activityId);
        ActivityCountSaveRequestDTO data = null;
        if (entity != null) {
            data = ActivityCountSaveRequestDTO.builder()
                    .activityCountId(entity.getActivityCountId())
                    .activityId(activityId)
                    .totalCount(entity.getTotalCount())
                    .dayCount(entity.getDayCount())
                    .monthCount(entity.getMonthCount())
                    .build();
        }
        return Response.<ActivityCountSaveRequestDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "sku/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveSku(@RequestBody ActivitySkuSaveRequestDTO request) {
        log.info("保存活动SKU activityId:{} sku:{}", request.getActivityId(), request.getSku());
        AdminActivitySkuEntity entity = AdminActivitySkuEntity.builder()
                .sku(request.getSku())
                .activityId(request.getActivityId())
                .activityCountId(request.getActivityCountId())
                .stockCount(request.getStockCount())
                .productAmount(request.getProductAmount())
                .build();
        adminActivityService.saveSku(entity);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "sku/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteSku(@RequestParam Long sku) {
        log.info("删除活动SKU sku:{}", sku);
        adminActivityService.deleteSku(sku);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "sku/list", method = RequestMethod.GET)
    @Override
    public Response<List<ActivitySkuResponseDTO>> skuList(@RequestParam Long activityId) {
        log.info("查询活动SKU列表 activityId:{}", activityId);
        List<AdminActivitySkuEntity> entities = adminActivityService.skuList(activityId);
        List<ActivitySkuResponseDTO> list = entities.stream()
                .map(e -> ActivitySkuResponseDTO.builder()
                        .sku(e.getSku())
                        .activityId(e.getActivityId())
                        .activityCountId(e.getActivityCountId())
                        .stockCount(e.getStockCount())
                        .stockCountSurplus(e.getStockCountSurplus())
                        .productAmount(e.getProductAmount())
                        .build())
                .collect(Collectors.toList());
        return Response.<List<ActivitySkuResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(list)
                .build();
    }

    @RequestMapping(value = "sku/stock", method = RequestMethod.POST)
    @Override
    public Response<Void> adjustSkuStock(@RequestBody ActivitySkuStockRequestDTO request) {
        log.info("调整SKU库存 sku:{} delta:{}", request.getSku(), request.getDelta());
        adminActivityService.adjustSkuStock(request.getSku(), request.getDelta());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    private AdminActivityEntity toActivityEntity(ActivityCreateRequestDTO dto) {
        return AdminActivityEntity.builder()
                .activityId(dto.getActivityId())
                .activityName(dto.getActivityName())
                .activityDesc(dto.getActivityDesc())
                .beginDateTime(parseDate(dto.getBeginDateTime()))
                .endDateTime(parseDate(dto.getEndDateTime()))
                .strategyId(dto.getStrategyId())
                .state(dto.getState())
                .build();
    }

    private ActivityDetailResponseDTO toDetailDTO(AdminActivityEntity entity) {
        return ActivityDetailResponseDTO.builder()
                .activityId(entity.getActivityId())
                .activityName(entity.getActivityName())
                .activityDesc(entity.getActivityDesc())
                .beginDateTime(formatDate(entity.getBeginDateTime()))
                .endDateTime(formatDate(entity.getEndDateTime()))
                .strategyId(entity.getStrategyId())
                .state(entity.getState())
                .createTime(formatDate(entity.getCreateTime()))
                .updateTime(formatDate(entity.getUpdateTime()))
                .build();
    }

    private ActivityPageResponseDTO toPageDTO(AdminActivityEntity entity) {
        return ActivityPageResponseDTO.builder()
                .activityId(entity.getActivityId())
                .activityName(entity.getActivityName())
                .activityDesc(entity.getActivityDesc())
                .beginDateTime(formatDate(entity.getBeginDateTime()))
                .endDateTime(formatDate(entity.getEndDateTime()))
                .strategyId(entity.getStrategyId())
                .state(entity.getState())
                .createTime(formatDate(entity.getCreateTime()))
                .build();
    }

    private static Date parseDate(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return DATE_FMT.parse(s); } catch (Exception e) { return null; }
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
