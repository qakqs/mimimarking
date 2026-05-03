package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.aggregate.AdminRuleTreeAggregate;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.trigger.api.dto.req.RuleTreeCreateRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeLineSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreeNodeSaveRequestDTO;
import cn.bugstack.trigger.api.dto.req.RuleTreePageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.RuleTreeDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RuleTreePageResponseDTO;
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
@RequestMapping("/admin/rule-tree")
public class AdminRuleTreeController implements cn.bugstack.trigger.api.IAdminRuleTreeService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminRuleTreeService adminRuleTreeService;

    public AdminRuleTreeController(cn.bugstack.domain.admin.service.IAdminRuleTreeService adminRuleTreeService) {
        this.adminRuleTreeService = adminRuleTreeService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public Response<Void> create(@RequestBody RuleTreeCreateRequestDTO request) {
        log.info("创建规则树开始 treeId:{}", request.getTreeId());
        adminRuleTreeService.create(toTreeEntity(request));
        log.info("创建规则树完成 treeId:{}", request.getTreeId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @Override
    public Response<Void> update(@RequestBody RuleTreeCreateRequestDTO request) {
        log.info("更新规则树开始 treeId:{}", request.getTreeId());
        adminRuleTreeService.update(toTreeEntity(request));
        log.info("更新规则树完成 treeId:{}", request.getTreeId());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Override
    public Response<Void> delete(@RequestParam String treeId) {
        log.info("删除规则树 treeId:{}", treeId);
        adminRuleTreeService.delete(treeId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @Override
    public Response<RuleTreeDetailResponseDTO> detail(@RequestParam String treeId) {
        log.info("查询规则树详情 treeId:{}", treeId);
        AdminRuleTreeAggregate aggregate = adminRuleTreeService.detail(treeId);
        RuleTreeDetailResponseDTO data = null;
        if (aggregate != null && aggregate.getTree() != null) {
            AdminRuleTreeEntity tree = aggregate.getTree();
            List<RuleTreeNodeSaveRequestDTO> nodes = aggregate.getNodes() != null
                    ? aggregate.getNodes().stream().map(this::toNodeDTO).collect(Collectors.toList())
                    : null;
            List<RuleTreeNodeLineSaveRequestDTO> lines = aggregate.getLines() != null
                    ? aggregate.getLines().stream().map(this::toLineDTO).collect(Collectors.toList())
                    : null;
            data = RuleTreeDetailResponseDTO.builder()
                    .treeId(tree.getTreeId())
                    .treeName(tree.getTreeName())
                    .treeDesc(tree.getTreeDesc())
                    .treeRootRuleKey(tree.getTreeRootRuleKey())
                    .nodes(nodes)
                    .lines(lines)
                    .build();
        }
        return Response.<RuleTreeDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<RuleTreePageResponseDTO>> list(@RequestBody RuleTreePageRequestDTO request) {
        log.info("查询规则树分页列表 pageNum:{}", request.getPageNum());
        List<AdminRuleTreeEntity> entities = adminRuleTreeService.list(
                request.getPageNum(), request.getPageSize(), request.getTreeName());
        int total = adminRuleTreeService.count(request.getTreeName());
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

    @RequestMapping(value = "node/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveNode(@RequestBody RuleTreeNodeSaveRequestDTO request) {
        log.info("保存规则树节点 treeId:{} ruleKey:{}", request.getTreeId(), request.getRuleKey());
        adminRuleTreeService.saveNode(toNodeEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "node/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteNode(@RequestParam Long id) {
        log.info("删除规则树节点 id:{}", id);
        adminRuleTreeService.deleteNode(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "line/save", method = RequestMethod.POST)
    @Override
    public Response<Void> saveLine(@RequestBody RuleTreeNodeLineSaveRequestDTO request) {
        log.info("保存规则树连线 treeId:{} from:{} to:{}",
                request.getTreeId(), request.getRuleNodeFrom(), request.getRuleNodeTo());
        adminRuleTreeService.saveLine(toLineEntity(request));
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @RequestMapping(value = "line/delete", method = RequestMethod.POST)
    @Override
    public Response<Void> deleteLine(@RequestParam Long id) {
        log.info("删除规则树连线 id:{}", id);
        adminRuleTreeService.deleteLine(id);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    private AdminRuleTreeEntity toTreeEntity(RuleTreeCreateRequestDTO dto) {
        return AdminRuleTreeEntity.builder()
                .treeId(dto.getTreeId())
                .treeName(dto.getTreeName())
                .treeDesc(dto.getTreeDesc())
                .treeRootRuleKey(dto.getTreeRootRuleKey())
                .build();
    }

    private AdminRuleTreeNodeEntity toNodeEntity(RuleTreeNodeSaveRequestDTO dto) {
        return AdminRuleTreeNodeEntity.builder()
                .id(dto.getId())
                .treeId(dto.getTreeId())
                .ruleKey(dto.getRuleKey())
                .ruleDesc(dto.getRuleDesc())
                .ruleValue(dto.getRuleValue())
                .build();
    }

    private RuleTreeNodeSaveRequestDTO toNodeDTO(AdminRuleTreeNodeEntity entity) {
        return RuleTreeNodeSaveRequestDTO.builder()
                .id(entity.getId())
                .treeId(entity.getTreeId())
                .ruleKey(entity.getRuleKey())
                .ruleDesc(entity.getRuleDesc())
                .ruleValue(entity.getRuleValue())
                .build();
    }

    private AdminRuleTreeNodeLineEntity toLineEntity(RuleTreeNodeLineSaveRequestDTO dto) {
        return AdminRuleTreeNodeLineEntity.builder()
                .id(dto.getId())
                .treeId(dto.getTreeId())
                .ruleNodeFrom(dto.getRuleNodeFrom())
                .ruleNodeTo(dto.getRuleNodeTo())
                .ruleLimitType(dto.getRuleLimitType())
                .ruleLimitValue(dto.getRuleLimitValue())
                .build();
    }

    private RuleTreeNodeLineSaveRequestDTO toLineDTO(AdminRuleTreeNodeLineEntity entity) {
        return RuleTreeNodeLineSaveRequestDTO.builder()
                .id(entity.getId())
                .treeId(entity.getTreeId())
                .ruleNodeFrom(entity.getRuleNodeFrom())
                .ruleNodeTo(entity.getRuleNodeTo())
                .ruleLimitType(entity.getRuleLimitType())
                .ruleLimitValue(entity.getRuleLimitValue())
                .build();
    }

    private static String formatDate(Date d) {
        if (d == null) return null;
        return DATE_FMT.format(d);
    }

}
