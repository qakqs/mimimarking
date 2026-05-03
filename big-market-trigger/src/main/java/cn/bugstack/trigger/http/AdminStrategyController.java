package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;
import cn.bugstack.trigger.api.dto.req.StrategyAwardSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyRuleSaveRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.StrategyDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyPageResponseDTO;
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
@RequestMapping("/admin/strategy")
public class AdminStrategyController implements cn.bugstack.trigger.api.IAdminStrategyService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminStrategyService adminStrategyService;

    public AdminStrategyController(cn.bugstack.domain.admin.service.IAdminStrategyService adminStrategyService) {
        this.adminStrategyService = adminStrategyService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public Response<Void> create(@RequestBody StrategyCreateRequestDTO request) {
        log.info("创建策略开始 strategyDesc:{}", request.getStrategyDesc());
        adminStrategyService.create(toStrategyEntity(request));
        log.info("创建策略完成");
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @Override
    public Response<Void> update(@RequestBody StrategyCreateRequestDTO request) {
        log.info("更新策略开始 strategyId:{}", request.getStrategyId());
        adminStrategyService.update(toStrategyEntity(request));
        log.info("更新策略完成 strategyId:{}", request.getStrategyId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Override
    public Response<Void> delete(@RequestParam Long strategyId) {
        log.info("删除策略 strategyId:{}", strategyId);
        adminStrategyService.delete(strategyId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @Override
    public Response<StrategyDetailResponseDTO> detail(@RequestParam Long strategyId) {
        log.info("查询策略详情 strategyId:{}", strategyId);
        AdminStrategyEntity entity = adminStrategyService.detail(strategyId);
        StrategyDetailResponseDTO data = null;
        if (entity != null) {
            data = StrategyDetailResponseDTO.builder()
                    .strategyId(entity.getStrategyId())
                    .strategyDesc(entity.getStrategyDesc())
                    .ruleModels(entity.getRuleModels())
                    .createTime(formatDate(entity.getCreateTime()))
                    .updateTime(formatDate(entity.getUpdateTime()))
                    .build();
        }
        return Response.<StrategyDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<StrategyPageResponseDTO>> list(@RequestBody StrategyPageRequestDTO request) {
        log.info("查询策略分页列表 pageNum:{}", request.getPageNum());
        List<AdminStrategyEntity> entities = adminStrategyService.list(
                request.getPageNum(), request.getPageSize(), request.getStrategyDesc());
        int total = adminStrategyService.count(request.getStrategyDesc());
        List<StrategyPageResponseDTO> list = entities.stream()
                .map(e -> StrategyPageResponseDTO.builder()
                        .strategyId(e.getStrategyId())
                        .strategyDesc(e.getStrategyDesc())
                        .ruleModels(e.getRuleModels())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<StrategyPageResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<StrategyPageResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "award/list", method = RequestMethod.GET)
    @Override
    public Response<List<StrategyAwardSaveRequestDTO>> awardList(@RequestParam Long strategyId) {
        log.info("查询策略奖品列表 strategyId:{}", strategyId);
        List<AdminStrategyAwardEntity> entities = adminStrategyService.awardList(strategyId);
        List<StrategyAwardSaveRequestDTO> list = entities.stream()
                .map(this::toAwardDTO).collect(Collectors.toList());
        return Response.<List<StrategyAwardSaveRequestDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(list)
                .build();
    }

    @RequestMapping(value = "award/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveAward(@RequestBody StrategyAwardSaveRequestDTO request) {
        log.info("保存策略奖品 strategyId:{} awardId:{}", request.getStrategyId(), request.getAwardId());
        adminStrategyService.saveAward(toAwardEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "award/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteAward(@RequestParam Long id) {
        log.info("删除策略奖品 id:{}", id);
        adminStrategyService.deleteAward(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule/list", method = RequestMethod.GET)
    @Override
    public Response<List<StrategyRuleSaveRequestDTO>> ruleList(@RequestParam Long strategyId) {
        log.info("查询策略规则列表 strategyId:{}", strategyId);
        List<AdminStrategyRuleEntity> entities = adminStrategyService.ruleList(strategyId);
        List<StrategyRuleSaveRequestDTO> list = entities.stream()
                .map(this::toRuleDTO).collect(Collectors.toList());
        return Response.<List<StrategyRuleSaveRequestDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(list)
                .build();
    }

    @RequestMapping(value = "rule/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveRule(@RequestBody StrategyRuleSaveRequestDTO request) {
        log.info("保存策略规则 strategyId:{} ruleModel:{}", request.getStrategyId(), request.getRuleModel());
        adminStrategyService.saveRule(toRuleEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteRule(@RequestParam Long id) {
        log.info("删除策略规则 id:{}", id);
        adminStrategyService.deleteRule(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    private AdminStrategyEntity toStrategyEntity(StrategyCreateRequestDTO dto) {
        return AdminStrategyEntity.builder()
                .strategyId(dto.getStrategyId())
                .strategyDesc(dto.getStrategyDesc())
                .ruleModels(dto.getRuleModels())
                .build();
    }

    private AdminStrategyAwardEntity toAwardEntity(StrategyAwardSaveRequestDTO dto) {
        return AdminStrategyAwardEntity.builder()
                .id(dto.getId())
                .strategyId(dto.getStrategyId())
                .awardId(dto.getAwardId())
                .awardTitle(dto.getAwardTitle())
                .awardSubtitle(dto.getAwardSubtitle())
                .awardCount(dto.getAwardCount())
                .awardRate(dto.getAwardRate())
                .ruleModels(dto.getRuleModels())
                .sort(dto.getSort())
                .build();
    }

    private StrategyAwardSaveRequestDTO toAwardDTO(AdminStrategyAwardEntity entity) {
        return StrategyAwardSaveRequestDTO.builder()
                .id(entity.getId())
                .strategyId(entity.getStrategyId())
                .awardId(entity.getAwardId())
                .awardTitle(entity.getAwardTitle())
                .awardSubtitle(entity.getAwardSubtitle())
                .awardCount(entity.getAwardCount())
                .awardRate(entity.getAwardRate())
                .ruleModels(entity.getRuleModels())
                .sort(entity.getSort())
                .build();
    }

    private AdminStrategyRuleEntity toRuleEntity(StrategyRuleSaveRequestDTO dto) {
        return AdminStrategyRuleEntity.builder()
                .id(dto.getId())
                .strategyId(dto.getStrategyId())
                .awardId(dto.getAwardId())
                .ruleType(dto.getRuleType())
                .ruleModel(dto.getRuleModel())
                .ruleValue(dto.getRuleValue())
                .ruleDesc(dto.getRuleDesc())
                .build();
    }

    private StrategyRuleSaveRequestDTO toRuleDTO(AdminStrategyRuleEntity entity) {
        return StrategyRuleSaveRequestDTO.builder()
                .id(entity.getId())
                .strategyId(entity.getStrategyId())
                .awardId(entity.getAwardId())
                .ruleType(entity.getRuleType())
                .ruleModel(entity.getRuleModel())
                .ruleValue(entity.getRuleValue())
                .ruleDesc(entity.getRuleDesc())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
