package cn.newness.imip.module.system.controller.app.user;

import cn.newness.imip.framework.common.pojo.CommonResult;
import cn.newness.imip.framework.datapermission.core.annotation.DataPermission;
import cn.newness.imip.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.newness.imip.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import cn.newness.imip.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import cn.newness.imip.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.newness.imip.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.newness.imip.framework.common.pojo.CommonResult.success;
import static cn.newness.imip.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.newness.imip.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

/**
 * @author machuran
 * @date 9/1/2024
 * @time 10:46 AM
 * @Description
 */
@Tag(name = "app端 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class AppUserProfileController {
    @Resource
    private AdminUserService userService;

    @GetMapping("/get")
    @Operation(summary = "获得登录用户信息")
    @DataPermission(enable = false) // 关闭数据权限，避免只查看自己时，查询不到部门。
    public CommonResult<UserProfileRespVO> getUserProfile() {
        UserProfileRespVO userProfileRespVO = userService.getUserProfile();
        return success(userProfileRespVO);
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }

    @RequestMapping(value = "/update-avatar",
            method = {RequestMethod.POST, RequestMethod.PUT}) // 解决 uni-app 不支持 Put 上传文件的问题
    @Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
        return success(avatar);
    }
}
