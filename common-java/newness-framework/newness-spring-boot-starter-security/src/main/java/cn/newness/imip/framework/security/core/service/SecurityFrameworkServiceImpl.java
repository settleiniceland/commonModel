package cn.newness.imip.framework.security.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.newness.imip.framework.security.core.LoginUser;
import cn.newness.imip.framework.security.core.util.SecurityFrameworkUtils;
import cn.newness.imip.module.system.api.permission.PermissionApi;
import lombok.AllArgsConstructor;

import java.util.Arrays;

import static cn.newness.imip.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 默认的 {@link SecurityFrameworkService} 实现类
 *
 * @author 新奇源码
 */
@AllArgsConstructor
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {

    private final PermissionApi permissionApi;

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        return permissionApi.hasAnyPermissions(getLoginUserId(), permissions);
    }

    @Override
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        return permissionApi.hasAnyRoles(getLoginUserId(), roles);
    }

    @Override
    public boolean hasScope(String scope) {
        return hasAnyScopes(scope);
    }

    @Override
    public boolean hasAnyScopes(String... scope) {
        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        if (user == null) {
            return false;
        }
        return CollUtil.containsAny(user.getScopes(), Arrays.asList(scope));
    }

}
