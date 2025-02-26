package cn.newness.imip.framework.mybatis.core.handler;

import cn.newness.imip.framework.mybatis.core.dataobject.BaseDO;
import cn.newness.imip.framework.mybatis.core.dataobject.DataPermissionDO;
import cn.newness.imip.framework.web.core.util.WebFrameworkUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 *
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author hexiaowu
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
//            if (Objects.isNull(baseDO.getCreateTime())) {
                baseDO.setCreateTime(current);//无论是否为空都插入
//            }
            // 更新时间为空，则以当前时间为更新时间
//            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(current);//无论是否为空都插入
//            }

            String loginUserName = WebFrameworkUtils.getLoginUserName();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.nonNull(loginUserName) && Objects.isNull(baseDO.getCreator())) {
                baseDO.setCreator(loginUserName);
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(loginUserName) && Objects.isNull(baseDO.getUpdater())) {
                baseDO.setUpdater(loginUserName);
            }
        }
        if(Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof DataPermissionDO) {
            DataPermissionDO permissionDO = (DataPermissionDO) metaObject.getOriginalObject();
            Long deptId = WebFrameworkUtils.getDeptId();
            if (Objects.nonNull(deptId)) {
                permissionDO.setDeptId(deptId);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
//        Object modifyTime = getFieldValByName("updateTime", metaObject);
//        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);//无论更新时间是否为空，都以当前时间为更新时间
//        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updater", metaObject);
        String loginUserName = WebFrameworkUtils.getLoginUserName();
        if (Objects.nonNull(loginUserName) && Objects.isNull(modifier)) {
            setFieldValByName("updater", loginUserName, metaObject);
        }
    }
}
