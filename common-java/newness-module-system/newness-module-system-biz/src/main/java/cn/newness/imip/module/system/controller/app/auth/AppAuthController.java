package cn.newness.imip.module.system.controller.app.auth;

import cn.newness.imip.framework.common.pojo.CommonResult;
import cn.newness.imip.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.newness.imip.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.newness.imip.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import cn.newness.imip.module.system.service.auth.AppAuthService;
import cn.newness.imip.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.newness.imip.framework.common.pojo.CommonResult.success;

/**
 * @author machuran
 * @date 9/1/2024
 * @time 9:22 AM
 * @Description
 */
@Tag(name = "app端 - 认证")
@RestController
@RequestMapping("/system/auth")
@Validated
@Slf4j
public class AppAuthController {
    @Resource
    private AppAuthService authService;
    @Resource
    private AdminUserService userService;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        authService.logout(request);
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        AuthPermissionInfoRespVO authPermissionInfo = userService.getPermissionInfo();
        return success(authPermissionInfo);
    }
}
