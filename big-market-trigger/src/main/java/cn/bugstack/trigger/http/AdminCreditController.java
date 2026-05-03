package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.aggregate.AdminCreditAdjustAggregate;
import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;
import cn.bugstack.trigger.api.dto.req.CreditAdjustRequestDTO;
import cn.bugstack.trigger.api.dto.req.CreditOrderPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.CreditAccountResponseDTO;
import cn.bugstack.trigger.api.dto.resp.CreditOrderResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.types.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/credit")
public class AdminCreditController implements cn.bugstack.trigger.api.IAdminCreditService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminCreditService adminCreditService;

    public AdminCreditController(cn.bugstack.domain.admin.service.IAdminCreditService adminCreditService) {
        this.adminCreditService = adminCreditService;
    }

    @RequestMapping(value = "account/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<CreditAccountResponseDTO>> accountList(@RequestBody CreditOrderPageRequestDTO request) {
        log.info("查询积分账户列表 pageNum:{}", request.getPageNum());
        List<AdminCreditAccountEntity> entities = adminCreditService.accountList(
                request.getPageNum(), request.getPageSize(), request.getUserId());
        int total = adminCreditService.accountCount(request.getUserId());
        List<CreditAccountResponseDTO> list = entities.stream()
                .map(e -> CreditAccountResponseDTO.builder()
                        .userId(e.getUserId())
                        .adjustAmount(e.getAvailableAmount())
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<CreditAccountResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<CreditAccountResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "account/detail", method = RequestMethod.GET)
    @Override
    public Response<CreditAccountResponseDTO> accountDetail(@RequestParam String userId) {
        log.info("查询积分账户详情 userId:{}", userId);
        AdminCreditAccountEntity entity = adminCreditService.accountDetail(userId);
        CreditAccountResponseDTO data = null;
        if (entity != null) {
            data = CreditAccountResponseDTO.builder()
                    .userId(entity.getUserId())
                    .adjustAmount(entity.getAvailableAmount())
                    .build();
        }
        return Response.<CreditAccountResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "adjust", method = RequestMethod.POST)
    @Override
    public Response<Void> adjust(@RequestBody CreditAdjustRequestDTO request) {
        log.info("手动调额 userId:{} amount:{} reason:{}",
                request.getUserId(), request.getAmount(), request.getReason());
        AdminCreditAccountEntity account = AdminCreditAccountEntity.builder()
                .userId(request.getUserId())
                .availableAmount(request.getAmount())
                .totalAmount(request.getAmount())
                .build();
        AdminCreditOrderEntity order = AdminCreditOrderEntity.builder()
                .userId(request.getUserId())
                .orderId(UUID.randomUUID().toString())
                .tradeName("手动调额")
                .tradeType(request.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "admin_incr" : "admin_decr")
                .tradeAmount(request.getAmount())
                .build();
        AdminCreditAdjustAggregate aggregate = AdminCreditAdjustAggregate.builder()
                .account(account).order(order).build();
        adminCreditService.adjust(aggregate);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "order/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<CreditOrderResponseDTO>> orderList(@RequestBody CreditOrderPageRequestDTO request) {
        log.info("查询积分交易流水 pageNum:{} userId:{}", request.getPageNum(), request.getUserId());
        List<AdminCreditOrderEntity> entities = adminCreditService.orderList(
                request.getPageNum(), request.getPageSize(), request.getUserId());
        int total = adminCreditService.orderCount(request.getUserId());
        List<CreditOrderResponseDTO> list = entities.stream()
                .map(e -> CreditOrderResponseDTO.builder()
                        .orderId(e.getOrderId())
                        .userId(e.getUserId())
                        .tradeName(e.getTradeName())
                        .tradeType(e.getTradeType())
                        .tradeAmount(e.getTradeAmount())
                        .outBusinessNo(e.getOutBusinessNo())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<CreditOrderResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<CreditOrderResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
