package cn.newness.imip.module.bpm.service.oa;


import cn.newness.imip.framework.common.pojo.PageResult;
import cn.newness.imip.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import cn.newness.imip.module.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import cn.newness.imip.module.bpm.dal.dataobject.oa.BpmOALeaveDO;

import jakarta.validation.Valid;

/**
 * 请假申请 Service 接口
 *
 * @author jason
 * @author 新奇源码
 */
public interface BpmOALeaveService {

    /**
     * 创建请假申请
     *
     * @param userId 用户编号
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLeave(Long userId, @Valid BpmOALeaveCreateReqVO createReqVO);

    /**
     * 更新请假申请的状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

    /**
     * 获得请假申请
     *
     * @param id 编号
     * @return 请假申请
     */
    BpmOALeaveDO getLeave(Long id);

    /**
     * 获得请假申请分页
     *
     * @param userId 用户编号
     * @param pageReqVO 分页查询
     * @return 请假申请分页
     */
    PageResult<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO);

}
