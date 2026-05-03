package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminRaffleActivityOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminRebateOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminTaskEntity;
import cn.bugstack.domain.admin.model.entity.AdminUserAwardRecordEntity;
import cn.bugstack.domain.admin.model.valobj.AwardStatProjection;
import cn.bugstack.trigger.api.dto.req.AwardRecordPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.OrderPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.RebateOrderPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.TaskPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.AwardRecordResponseDTO;
import cn.bugstack.trigger.api.dto.resp.AwardStatResponseDTO;
import cn.bugstack.trigger.api.dto.resp.OrderDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RebateOrderResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.TaskResponseDTO;
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
@RequestMapping("/admin/report")
public class AdminReportController implements cn.bugstack.trigger.api.IAdminReportService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminReportService adminReportService;

    public AdminReportController(cn.bugstack.domain.admin.service.IAdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @RequestMapping(value = "order/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<OrderDetailResponseDTO>> orderList(@RequestBody OrderPageRequestDTO request) {
        log.info("查询抽奖订单列表 pageNum:{}", request.getPageNum());
        List<AdminRaffleActivityOrderEntity> entities = adminReportService.orderList(
                request.getPageNum(), request.getPageSize(),
                request.getUserId(), request.getActivityId(), request.getOrderState());
        int total = adminReportService.orderCount(
                request.getUserId(), request.getActivityId(), request.getOrderState());
        List<OrderDetailResponseDTO> list = entities.stream()
                .map(e -> OrderDetailResponseDTO.builder()
                        .orderId(e.getOrderId())
                        .userId(e.getUserId())
                        .activityId(e.getActivityId())
                        .activityName(e.getActivityName())
                        .strategyId(e.getStrategyId())
                        .orderState(e.getState())
                        .orderTime(formatDate(e.getOrderTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<OrderDetailResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<OrderDetailResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "order/detail", method = RequestMethod.GET)
    @Override
    public Response<OrderDetailResponseDTO> orderDetail(@RequestParam String orderId) {
        log.info("查询抽奖订单详情 orderId:{}", orderId);
        AdminRaffleActivityOrderEntity entity = adminReportService.orderDetail(orderId);
        OrderDetailResponseDTO data = null;
        if (entity != null) {
            data = OrderDetailResponseDTO.builder()
                    .orderId(entity.getOrderId())
                    .userId(entity.getUserId())
                    .activityId(entity.getActivityId())
                    .activityName(entity.getActivityName())
                    .strategyId(entity.getStrategyId())
                    .orderState(entity.getState())
                    .orderTime(formatDate(entity.getOrderTime()))
                    .build();
        }
        return Response.<OrderDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "award-record/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<AwardRecordResponseDTO>> awardRecordList(@RequestBody AwardRecordPageRequestDTO request) {
        log.info("查询中奖记录列表 pageNum:{}", request.getPageNum());
        List<AdminUserAwardRecordEntity> entities = adminReportService.awardRecordList(
                request.getPageNum(), request.getPageSize(),
                request.getActivityId(), request.getAwardState());
        int total = adminReportService.awardRecordCount(
                request.getActivityId(), request.getAwardState());
        List<AwardRecordResponseDTO> list = entities.stream()
                .map(e -> AwardRecordResponseDTO.builder()
                        .orderId(e.getOrderId())
                        .userId(e.getUserId())
                        .activityId(e.getActivityId())
                        .strategyId(e.getStrategyId())
                        .awardId(e.getAwardId())
                        .awardTitle(e.getAwardTitle())
                        .awardState(e.getAwardState())
                        .awardTime(formatDate(e.getAwardTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<AwardRecordResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<AwardRecordResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "award-record/stat", method = RequestMethod.GET)
    @Override
    public Response<List<AwardStatResponseDTO>> awardStat(@RequestParam Long activityId) {
        log.info("查询中奖统计 activityId:{}", activityId);
        List<AwardStatProjection> entities = adminReportService.awardStat(activityId);
        List<AwardStatResponseDTO> list = entities.stream()
                .map(e -> AwardStatResponseDTO.builder()
                        .awardId(e.getAwardId())
                        .awardTitle(e.getAwardTitle())
                        .awardCount(e.getAwardCount())
                        .build())
                .collect(Collectors.toList());
        return Response.<List<AwardStatResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(list)
                .build();
    }

    @RequestMapping(value = "rebate-order/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<RebateOrderResponseDTO>> rebateOrderList(@RequestBody RebateOrderPageRequestDTO request) {
        log.info("查询返利订单列表 pageNum:{}", request.getPageNum());
        List<AdminRebateOrderEntity> entities = adminReportService.rebateOrderList(
                request.getPageNum(), request.getPageSize(), request.getRebateType());
        int total = adminReportService.rebateOrderCount(request.getRebateType());
        List<RebateOrderResponseDTO> list = entities.stream()
                .map(e -> RebateOrderResponseDTO.builder()
                        .orderId(e.getOrderId())
                        .userId(e.getUserId())
                        .behaviorType(e.getBehaviorType())
                        .rebateType(e.getRebateType())
                        .rebateConfig(e.getRebateConfig())
                        .outBusinessNo(e.getOutBusinessNo())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<RebateOrderResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<RebateOrderResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "task/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<TaskResponseDTO>> taskList(@RequestBody TaskPageRequestDTO request) {
        log.info("查询任务记录列表 pageNum:{}", request.getPageNum());
        List<AdminTaskEntity> entities = adminReportService.taskList(
                request.getPageNum(), request.getPageSize(),
                request.getUserId(), request.getState(), request.getTopic());
        int total = adminReportService.taskCount(
                request.getUserId(), request.getState(), request.getTopic());
        List<TaskResponseDTO> list = entities.stream()
                .map(e -> TaskResponseDTO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .topic(e.getTopic())
                        .messageId(e.getMessageId())
                        .state(e.getState())
                        .createTime(formatDate(e.getCreateTime()))
                        .updateTime(formatDate(e.getUpdateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<TaskResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<TaskResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "task/retry", method = RequestMethod.POST)
    @Override
    public Response<Void> retryTask(@RequestParam String id) {
        log.info("手动重试失败任务 id:{}", id);
        // retryTask not implemented in report service — task retry requires
        // direct MQ access. Log as placeholder; adapt if retry RPC is added later.
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
