package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminUserEntity;
import cn.bugstack.trigger.api.dto.req.UserDisableRequestDTO;
import cn.bugstack.trigger.api.dto.req.UserPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.UserDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.UserPageResponseDTO;
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
@RequestMapping("/admin/user")
public class AdminUserController implements cn.bugstack.trigger.api.IAdminUserService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final cn.bugstack.domain.admin.service.IAdminUserService adminUserService;

    public AdminUserController(cn.bugstack.domain.admin.service.IAdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @Override
    public Response<PageResponseDTO<UserPageResponseDTO>> list(@RequestBody UserPageRequestDTO request) {
        log.info("查询用户分页列表 pageNum:{}", request.getPageNum());
        String keyword = request.getUsername() != null ? request.getUsername() : request.getPhone();
        List<AdminUserEntity> entities = adminUserService.list(
                request.getPageNum(), request.getPageSize(), keyword, request.getStatus());
        int total = adminUserService.count(keyword, request.getStatus());
        List<UserPageResponseDTO> list = entities.stream()
                .map(e -> UserPageResponseDTO.builder()
                        .userId(e.getUserId())
                        .username(e.getUsername())
                        .name(e.getName())
                        .phone(e.getPhone())
                        .status(e.getStatus())
                        .createTime(formatDate(e.getCreateTime()))
                        .build())
                .collect(Collectors.toList());
        return Response.<PageResponseDTO<UserPageResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(PageResponseDTO.<UserPageResponseDTO>builder().total((long) total).list(list).build())
                .build();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @Override
    public Response<UserDetailResponseDTO> detail(@RequestParam String userId) {
        log.info("查询用户详情 userId:{}", userId);
        AdminUserEntity entity = adminUserService.detail(userId);
        UserDetailResponseDTO data = null;
        if (entity != null) {
            data = UserDetailResponseDTO.builder()
                    .userId(entity.getUserId())
                    .username(entity.getUsername())
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .status(entity.getStatus())
                    .createTime(formatDate(entity.getCreateTime()))
                    .updateTime(formatDate(entity.getUpdateTime()))
                    .build();
        }
        return Response.<UserDetailResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    @RequestMapping(value = "disable", method = RequestMethod.POST)
    @Override
    public Response<Void> disable(@RequestBody UserDisableRequestDTO request) {
        log.info("禁用/启用用户 userId:{} status:{}", request.getUserId(), request.getStatus());
        adminUserService.disable(request.getUserId(), Integer.parseInt(request.getStatus()));
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
