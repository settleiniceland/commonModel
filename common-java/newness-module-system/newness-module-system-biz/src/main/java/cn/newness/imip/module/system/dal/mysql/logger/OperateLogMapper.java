package cn.newness.imip.module.system.dal.mysql.logger;

import cn.newness.imip.framework.common.pojo.PageResult;
import cn.newness.imip.framework.mybatis.core.mapper.BaseMapperX;
import cn.newness.imip.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.newness.imip.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.newness.imip.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import cn.newness.imip.module.system.dal.dataobject.logger.OperateLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper extends BaseMapperX<OperateLogDO> {

    default PageResult<OperateLogDO> selectPage(OperateLogPageReqVO pageReqDTO) {
        return selectPage(pageReqDTO, new LambdaQueryWrapperX<OperateLogDO>()
                .eqIfPresent(OperateLogDO::getUserId, pageReqDTO.getUserId())
                .eqIfPresent(OperateLogDO::getBizId, pageReqDTO.getBizId())
                .likeIfPresent(OperateLogDO::getType, pageReqDTO.getType())
                .likeIfPresent(OperateLogDO::getSubType, pageReqDTO.getSubType())
                .likeIfPresent(OperateLogDO::getAction, pageReqDTO.getAction())
                .betweenIfPresent(OperateLogDO::getCreateTime, pageReqDTO.getCreateTime())
                .orderByDesc(OperateLogDO::getId));
    }

    default PageResult<OperateLogDO> selectPage(OperateLogPageReqDTO pageReqDTO) {
        return selectPage(pageReqDTO, new LambdaQueryWrapperX<OperateLogDO>()
                .eqIfPresent(OperateLogDO::getType, pageReqDTO.getType())
                .eqIfPresent(OperateLogDO::getBizId, pageReqDTO.getBizId())
                .eqIfPresent(OperateLogDO::getUserId, pageReqDTO.getUserId())
                .orderByDesc(OperateLogDO::getId));
    }

}
