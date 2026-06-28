package cn.bugstack.trigger.http;

import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.service.ILoginService;
import cn.bugstack.domain.user.service.IRegisterService;
import cn.bugstack.trigger.api.IUserService;
import cn.bugstack.trigger.api.dto.req.LoginRequestDTO;
import cn.bugstack.trigger.api.dto.req.LogoutRequestDTO;
import cn.bugstack.trigger.api.dto.req.RegisterRequestDTO;
import cn.bugstack.trigger.api.dto.resp.LoginResponseDTO;
import cn.bugstack.trigger.api.dto.resp.RegisterResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("/api/user")
public class UserController implements IUserService {
    private static final Log log = Log.get(UserController.class);

    @Resource
    private IRegisterService registerService;

    @Resource
    private ILoginService loginService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @Override
    public Response<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            log.info("用户注册开始 username:{}", request.getUsername());
            UserEntity user = registerService.register(request.getUsername(), request.getPassword());
            RegisterResponseDTO dto = RegisterResponseDTO.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .build();
            log.info("用户注册完成 userId:{} username:{}", user.getUserId(), user.getUsername());
            return Response.<RegisterResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(dto)
                    .build();
        } catch (AppException e) {
            log.error("用户注册失败 username:{} {}", request.getUsername(), e.getInfo());
            return Response.<RegisterResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("用户注册异常 username:{}", request.getUsername(), e);
            return Response.<RegisterResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @Override
    public Response<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            log.info("用户登录开始 username:{}", request.getUsername());
            String token = loginService.login(request.getUsername(), request.getPassword());
            UserEntity user = loginService.queryUserByToken(token);
            LoginResponseDTO dto = LoginResponseDTO.builder()
                    .token(token)
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .build();
            log.info("用户登录完成 userId:{} username:{}", user.getUserId(), user.getUsername());
            return Response.<LoginResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(dto)
                    .build();
        } catch (AppException e) {
            log.error("用户登录失败 username:{} {}", request.getUsername(), e.getInfo());
            return Response.<LoginResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("用户登录异常 username:{}", request.getUsername(), e);
            return Response.<LoginResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Response<Void> logout(@RequestBody LogoutRequestDTO request) {
        try {
            log.info("用户登出开始");
            loginService.logout(request.getToken());
            log.info("用户登出完成");
            return Response.<Void>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (AppException e) {
            log.error("用户登出失败 {}", e.getInfo());
            return Response.<Void>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("用户登出异常", e);
            return Response.<Void>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
