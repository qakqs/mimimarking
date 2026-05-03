package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminDailyBehaviorRebateEntity;
import cn.bugstack.trigger.api.dto.req.RebateConfigPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.RebateConfigSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RebateConfigToggleRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RebateConfigResponseDTO;
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
@RequestMapping("/admin/rebate")
public class AdminRebateController implements cn.bugstack.trigger.api.IAdminRebateService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminRebateService adminRebateService;

    public AdminRebateController(cn.bugstack.domain.admin.service.IAdminRebateService adminRebateService) {
        this.adminRebateService = adminRebateService;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Override
    public Response<Void> save(@RequestBody RebateConfigSaveRequestDTO request) {
        log.info("保存返利配置 behaviorType:{}", request.getBehaviorType());
        adminRebateService.save(toEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Override
    public Response<Void> delete(@RequestParam Long id) {
        log.info("删除返利配置 id:{}", id);
        adminRebateService.delete(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "toggle", method = RequestMethod.POST)
    @Override
    public Response<Void> toggle(@RequestBody RebateConfigToggleRequestDTO request) {
        log.info("启停返利配置 id:{} state:{}", request.getId(), request.getState());
        adminRebateService.toggle(request.getId(), Integer.parseInt(request.getState()));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<RebateConfigResponseDTO>> list(@RequestBody RebateConfigPageRequestDTO request) {
        log.info("查询返利配置分页列表 pageNum:{}", request.getPageNum());
        List<AdminDailyBehaviorRebateEntity> entities = adminRebateService.list(
                request.getPageNum(), request.getPageSize(),
                request.getBehaviorType(), request.getState());
        int total = adminRebateService.count(request.getBehaviorType(), request.getState());
        List<RebateConfigResponseDTO> list = entities.stream()
                .map(e -> RebateConfigResponseDTO.builder()
                        .id(e.getId())
                        .behaviorType(e.getBehaviorType())
                        .rebateDesc(e.getRebateDesc())
                        .rebateType(e.getRebateType())
                        .rebateConfig(e.getRebateConfig())
                        .state(e.getState())
                        .createTime(formatDate(e.getCreateTime()))
                        .updateTime(formatDate(e.getUpdateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<RebateConfigResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<RebateConfigResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    private AdminDailyBehaviorRebateEntity toEntity(RebateConfigSaveRequestDTO dto) {
        return AdminDailyBehaviorRebateEntity.builder()
                .id(dto.getId())
                .behaviorType(dto.getBehaviorType())
                .rebateDesc(dto.getRebateDesc())
                .rebateType(dto.getRebateType())
                .rebateConfig(dto.getRebateConfig())
                .state(dto.getState())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
