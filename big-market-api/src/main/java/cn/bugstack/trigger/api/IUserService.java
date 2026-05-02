package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.req.LoginRequestDTO;
import cn.bugstack.trigger.api.dto.req.RegisterRequestDTO;
import cn.bugstack.trigger.api.dto.resp.LoginResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RegisterResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;

/**
 * 用户服务 API 接口
 */
public interface IUserService {

    /** 用户注册 */
    Response<RegisterResponseDTO> register(RegisterRequestDTO request);

    /** 用户登录 */
    Response<LoginResponseDTO> login(LoginRequestDTO request);

}
