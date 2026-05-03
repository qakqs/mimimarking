package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.UserDisableRequestDTO;
import cn.bugstack.trigger.api.dto.req.UserPageRequestDTO;
import cn.bugstack.trigger.api.dto.resp.PageResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.UserDetailResponseDTO;
import cn.bugstack.trigger.api.dto.resp.UserPageResponseDTO;

/**
 * 后台用户管理 API 接口
 */
public interface IAdminUserService {

    /** 用户分页列表 */
    Response<PageResponseDTO<UserPageResponseDTO>> list(UserPageRequestDTO request);

    /** 用户详情 */
    Response<UserDetailResponseDTO> detail(String userId);

    /** 禁用/启用用户 */
    Response<Void> disable(UserDisableRequestDTO request);

}
