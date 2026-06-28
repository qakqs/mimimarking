package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminAwardEntity;
import cn.bugstack.trigger.api.dto.req.AwardCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.AwardPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.AwardDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.AwardPageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.common.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/award")
public class AdminAwardController implements cn.bugstack.trigger.api.IAdminAwardService {
    private static final Log log = Log.get(AdminAwardController.class);

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminAwardService adminAwardService;

    public AdminAwardController(cn.bugstack.domain.admin.service.IAdminAwardService adminAwardService) {
        this.adminAwardService = adminAwardService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public Response<Void> create(@RequestBody AwardCreateRequestDTO request) {
        log.info("创建奖品开始 awardId:{}", request.getAwardId());
        adminAwardService.create(toAwardEntity(request));
        log.info("创建奖品完成 awardId:{}", request.getAwardId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @Override
    public Response<Void> update(@RequestBody AwardCreateRequestDTO request) {
        log.info("更新奖品开始 awardId:{}", request.getAwardId());
        adminAwardService.update(toAwardEntity(request));
        log.info("更新奖品完成 awardId:{}", request.getAwardId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Override
    public Response<Void> delete(@RequestParam Integer awardId) {
        log.info("删除奖品 awardId:{}", awardId);
        adminAwardService.delete(awardId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @Override
    public Response<AwardDetailResponseDTO> detail(@RequestParam Integer awardId) {
        log.info("查询奖品详情 awardId:{}", awardId);
        AdminAwardEntity entity = adminAwardService.detail(awardId);
        AwardDetailResponseDTO data = null;
        if (entity != null) {
            data = AwardDetailResponseDTO.builder()
                    .awardId(entity.getAwardId())
                    .awardKey(entity.getAwardKey())
                    .awardConfig(entity.getAwardConfig())
                    .awardDesc(entity.getAwardDesc())
                    .createTime(formatDate(entity.getCreateTime()))
                    .updateTime(formatDate(entity.getUpdateTime()))
                    .build();
        }
        return Response.<AwardDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<AwardPageResponseDTO>> list(@RequestBody AwardPageRequestDTO request) {
        log.info("查询奖品分页列表 pageNum:{}", request.getPageNum());
        List<AdminAwardEntity> entities = adminAwardService.list(
                request.getPageNum(), request.getPageSize(), request.getAwardDesc());
        int total = adminAwardService.count(request.getAwardDesc());
        List<AwardPageResponseDTO> list = entities.stream()
                .map(e -> AwardPageResponseDTO.builder()
                        .awardId(e.getAwardId())
                        .awardKey(e.getAwardKey())
                        .awardConfig(e.getAwardConfig())
                        .awardDesc(e.getAwardDesc())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<AwardPageResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<AwardPageResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    private AdminAwardEntity toAwardEntity(AwardCreateRequestDTO dto) {
        return AdminAwardEntity.builder()
                .awardId(dto.getAwardId())
                .awardKey(dto.getAwardKey())
                .awardConfig(dto.getAwardConfig())
                .awardDesc(dto.getAwardDesc())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
