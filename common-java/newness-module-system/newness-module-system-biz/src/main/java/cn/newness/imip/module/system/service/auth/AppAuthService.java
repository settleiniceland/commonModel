package cn.newness.imip.module.system.service.auth;

import cn.newness.imip.module.system.controller.admin.auth.vo.*;
import cn.newness.imip.module.system.dal.dataobject.user.AdminUserDO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * @author machuran
 * @date 9/1/2024
 * @time 2:37 PM
 * @Description
 */
public interface AppAuthService {
    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    AdminUserDO authenticate(String username, String password);

    /**
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * 基于 token 退出登录
     *
     * @param token token
     * @param logType 登出类型
     */
    void logout(String token, Integer logType);
    /**
     * 基于 token 退出登录【对登出功能做个包装，供移动端使用】
     *
     * @param [request]
     * @author machuran
     * @date 9/1/2024
     * @Return void
     */
    void logout(HttpServletRequest request);
    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    AuthLoginRespVO refreshToken(String refreshToken);
}
