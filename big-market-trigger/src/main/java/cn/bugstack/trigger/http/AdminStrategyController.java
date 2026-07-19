package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeVO;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeVO;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.trigger.api.dto.req.RuleTreeCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeLineSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreePageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyAwardSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyPageRequestDTO;
import cn.bugstack.trigger.api.dto.req.StrategyRuleSaveRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.RuleTreeDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreeNodeLineResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreeNodeResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreePageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.StrategyPageResponseDTO;
import cn.bugstack.types.common.ResponseCode;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/strategy")
public class AdminStrategyController implements cn.bugstack.trigger.api.IAdminStrategyService {
    private static final Log log = Log.get(AdminStrategyController.class);

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminStrategyService adminStrategyService;

    @Resource
    private IStrategyRepository strategyRepository;

    public AdminStrategyController(cn.bugstack.domain.admin.service.IAdminStrategyService adminStrategyService) {
        this.adminStrategyService = adminStrategyService;
    }

    // ====== Strategy ======


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

    // ====== Strategy Award ======

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

    // ====== Strategy Rule ======

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

    // ====== Rule Tree ======

    @RequestMapping(value = "rule-tree/create", method = RequestMethod.POST)
    @Override
    public Response<Void> createRuleTree(@RequestBody RuleTreeCreateRequestDTO request) {
        log.info("创建规则树开始 treeId:{}", request.getTreeId());
        adminStrategyService.createRuleTree(toRuleTreeEntity(request));
        log.info("创建规则树完成 treeId:{}", request.getTreeId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/update", method = RequestMethod.POST)
    @Override
    public Response<Void> updateRuleTree(@RequestBody RuleTreeCreateRequestDTO request) {
        log.info("更新规则树开始 treeId:{}", request.getTreeId());
        adminStrategyService.updateRuleTree(toRuleTreeEntity(request));
        log.info("更新规则树完成 treeId:{}", request.getTreeId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteRuleTree(@RequestParam String treeId) {
        log.info("删除规则树 treeId:{}", treeId);
        adminStrategyService.deleteRuleTree(treeId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/detail", method = RequestMethod.GET)
    @Override
    public Response<RuleTreeDetailResponseDTO> detailRuleTree(@RequestParam String treeId) {
        log.info("查询规则树详情 treeId:{}", treeId);
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(treeId);
        if (ruleTreeVO == null) {
            return Response.<RuleTreeDetailResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(null)
                    .build();
        }

        Map<String, RuleTreeNodeResponseDTO> treeNodeMap = ruleTreeVO.getTreeNodeMap()
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toRuleTreeNodeResponseDTO(e.getValue())
                ));

        RuleTreeDetailResponseDTO data = RuleTreeDetailResponseDTO.builder()
                .treeId(ruleTreeVO.getTreeId())
                .treeName(ruleTreeVO.getTreeName())
                .treeDesc(ruleTreeVO.getTreeDesc())
                .treeRootRuleNode(ruleTreeVO.getTreeRootRuleNode())
                .treeNodeMap(treeNodeMap)
                .build();

        return Response.<RuleTreeDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "rule-tree/list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<RuleTreePageResponseDTO>> listRuleTree(@RequestBody RuleTreePageRequestDTO request) {
        log.info("查询规则树分页列表 pageNum:{}", request.getPageNum());
        List<AdminRuleTreeEntity> entities = adminStrategyService.listRuleTree(
                request.getPageNum(), request.getPageSize(), request.getTreeName());
        int total = adminStrategyService.countRuleTree(request.getTreeName());
        List<RuleTreePageResponseDTO> list = entities.stream()
                .map(e -> RuleTreePageResponseDTO.builder()
                        .treeId(e.getTreeId())
                        .treeName(e.getTreeName())
                        .treeDesc(e.getTreeDesc())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<RuleTreePageResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<RuleTreePageResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "rule-tree/node/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveRuleTreeNode(@RequestBody RuleTreeNodeSaveRequestDTO request) {
        log.info("保存规则树节点 treeId:{} ruleKey:{}", request.getTreeId(), request.getRuleKey());
        adminStrategyService.saveRuleTreeNode(toRuleTreeNodeEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/node/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteRuleTreeNode(@RequestParam Long id) {
        log.info("删除规则树节点 id:{}", id);
        adminStrategyService.deleteRuleTreeNode(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/line/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveRuleTreeNodeLine(@RequestBody RuleTreeNodeLineSaveRequestDTO request) {
        log.info("保存规则树连线 treeId:{} from:{} to:{}",
                request.getTreeId(), request.getRuleNodeFrom(), request.getRuleNodeTo());
        adminStrategyService.saveRuleTreeNodeLine(toRuleTreeNodeLineEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "rule-tree/line/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteRuleTreeNodeLine(@RequestParam Long id) {
        log.info("删除规则树连线 id:{}", id);
        adminStrategyService.deleteRuleTreeNodeLine(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    // ====== Conversion methods ======

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

    private AdminRuleTreeEntity toRuleTreeEntity(RuleTreeCreateRequestDTO dto) {
        return AdminRuleTreeEntity.builder()
                .treeId(dto.getTreeId())
                .treeName(dto.getTreeName())
                .treeDesc(dto.getTreeDesc())
                .treeRootRuleKey(dto.getTreeRootRuleKey())
                .build();
    }

    private AdminRuleTreeNodeEntity toRuleTreeNodeEntity(RuleTreeNodeSaveRequestDTO dto) {
        return AdminRuleTreeNodeEntity.builder()
                .id(dto.getId())
                .treeId(dto.getTreeId())
                .ruleKey(dto.getRuleKey())
                .ruleDesc(dto.getRuleDesc())
                .ruleValue(dto.getRuleValue())
                .build();
    }

    private AdminRuleTreeNodeLineEntity toRuleTreeNodeLineEntity(RuleTreeNodeLineSaveRequestDTO dto) {
        return AdminRuleTreeNodeLineEntity.builder()
                .id(dto.getId())
                .treeId(dto.getTreeId())
                .ruleNodeFrom(dto.getRuleNodeFrom())
                .ruleNodeTo(dto.getRuleNodeTo())
                .ruleLimitType(dto.getRuleLimitType())
                .ruleLimitValue(dto.getRuleLimitValue())
                .build();
    }

    private RuleTreeNodeResponseDTO toRuleTreeNodeResponseDTO(RuleTreeNodeVO vo) {
        List<RuleTreeNodeLineResponseDTO> lines = vo.getTreeNodeLineVOList() != null
                ? vo.getTreeNodeLineVOList().stream()
                    .map(this::toRuleTreeNodeLineResponseDTO)
                    .collect(Collectors.toList())
                : null;
        return RuleTreeNodeResponseDTO.builder()
                .treeId(vo.getTreeId())
                .ruleKey(vo.getRuleKey())
                .ruleDesc(vo.getRuleDesc())
                .ruleValue(vo.getRuleValue())
                .treeNodeLineVOList(lines)
                .build();
    }

    private RuleTreeNodeLineResponseDTO toRuleTreeNodeLineResponseDTO(RuleTreeNodeLineVO vo) {
        return RuleTreeNodeLineResponseDTO.builder()
                .treeId(vo.getTreeId())
                .ruleNodeFrom(vo.getRuleNodeFrom())
                .ruleNodeTo(vo.getRuleNodeTo())
                .ruleLimitType(vo.getRuleLimitType() != null ? vo.getRuleLimitType().name() : null)
                .ruleLimitValue(vo.getRuleLimitValue() != null ? vo.getRuleLimitValue().name() : null)
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
