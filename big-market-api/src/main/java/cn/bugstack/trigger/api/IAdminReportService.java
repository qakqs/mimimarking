package cn.bugstack.trigger.api;

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

import java.util.List;

/**
 * 后台数据报表 API 接口
 */
public interface IAdminReportService {

    /** 抽奖订单分页列表 */
    Response<PageResponseDTO<OrderDetailResponseDTO>> orderList(OrderPageRequestDTO request);

    /** 抽奖订单详情 */
    Response<OrderDetailResponseDTO> orderDetail(String orderId);

    /** 中奖记录分页列表 */
    Response<PageResponseDTO<AwardRecordResponseDTO>> awardRecordList(AwardRecordPageRequestDTO request);

    /** 中奖统计 */
    Response<List<AwardStatResponseDTO>> awardStat(Long activityId);

    /** 返利订单分页列表 */
    Response<PageResponseDTO<RebateOrderResponseDTO>> rebateOrderList(RebateOrderPageRequestDTO request);

    /** 任务记录分页列表 */
    Response<PageResponseDTO<TaskResponseDTO>> taskList(TaskPageRequestDTO request);

    /** 手动重试失败任务 */
    Response<Void> retryTask(String id);

}
