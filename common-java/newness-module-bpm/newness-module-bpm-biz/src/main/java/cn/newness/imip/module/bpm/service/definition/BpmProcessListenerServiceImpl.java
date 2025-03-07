package cn.newness.imip.module.bpm.service.definition;

import cn.hutool.core.util.StrUtil;
import cn.newness.imip.framework.common.pojo.PageResult;
import cn.newness.imip.framework.common.util.object.BeanUtils;
import cn.newness.imip.module.bpm.controller.admin.definition.vo.listener.BpmProcessListenerPageReqVO;
import cn.newness.imip.module.bpm.controller.admin.definition.vo.listener.BpmProcessListenerSaveReqVO;
import cn.newness.imip.module.bpm.dal.dataobject.definition.BpmProcessListenerDO;
import cn.newness.imip.module.bpm.dal.mysql.definition.BpmProcessListenerMapper;
import cn.newness.imip.module.bpm.enums.definition.BpmProcessListenerType;
import cn.newness.imip.module.bpm.enums.definition.BpmProcessListenerValueType;
import jakarta.annotation.Resource;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.newness.imip.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.newness.imip.module.bpm.enums.ErrorCodeConstants.*;

/**
 * BPM 流程监听器 Service 实现类
 *
 * @author 新奇源码
 */
@Service
@Validated
public class BpmProcessListenerServiceImpl implements BpmProcessListenerService {

    @Resource
    private BpmProcessListenerMapper processListenerMapper;

    @Override
    public Long createProcessListener(BpmProcessListenerSaveReqVO createReqVO) {
        // 校验
        validateCreateProcessListenerValue(createReqVO);
        // 插入
        BpmProcessListenerDO processListener = BeanUtils.toBean(createReqVO, BpmProcessListenerDO.class);
        processListenerMapper.insert(processListener);
        return processListener.getId();
    }

    @Override
    public void updateProcessListener(BpmProcessListenerSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessListenerExists(updateReqVO.getId());
        validateCreateProcessListenerValue(updateReqVO);
        // 更新
        BpmProcessListenerDO updateObj = BeanUtils.toBean(updateReqVO, BpmProcessListenerDO.class);
        processListenerMapper.updateById(updateObj);
    }

    private void validateCreateProcessListenerValue(BpmProcessListenerSaveReqVO createReqVO) {
        // class 类型
        if (createReqVO.getValueType().equals(BpmProcessListenerValueType.CLASS.getType())) {
            try {
                Class<?> clazz = Class.forName(createReqVO.getValue());
                if (createReqVO.getType().equals(BpmProcessListenerType.EXECUTION.getType())
                    && !JavaDelegate.class.isAssignableFrom(clazz)) {
                    throw exception(PROCESS_LISTENER_CLASS_IMPLEMENTS_ERROR, createReqVO.getValue(),
                            JavaDelegate.class.getName());
                } else if (createReqVO.getType().equals(BpmProcessListenerType.TASK.getType())
                    && !TaskListener.class.isAssignableFrom(clazz)) {
                    throw exception(PROCESS_LISTENER_CLASS_IMPLEMENTS_ERROR, createReqVO.getValue(),
                            TaskListener.class.getName());
                }
            } catch (ClassNotFoundException e) {
                throw exception(PROCESS_LISTENER_CLASS_NOT_FOUND, createReqVO.getValue());
            }
            return;
        }
        // 表达式
        if (!StrUtil.startWith(createReqVO.getValue(), "${") || !StrUtil.endWith(createReqVO.getValue(), "}")) {
            throw exception(PROCESS_LISTENER_EXPRESSION_INVALID, createReqVO.getValue());
        }
    }

    @Override
    public void deleteProcessListener(Long id) {
        // 校验存在
        validateProcessListenerExists(id);
        // 删除
        processListenerMapper.deleteById(id);
    }

    private void validateProcessListenerExists(Long id) {
        if (processListenerMapper.selectById(id) == null) {
            throw exception(PROCESS_LISTENER_NOT_EXISTS);
        }
    }

    @Override
    public BpmProcessListenerDO getProcessListener(Long id) {
        return processListenerMapper.selectById(id);
    }

    @Override
    public PageResult<BpmProcessListenerDO> getProcessListenerPage(BpmProcessListenerPageReqVO pageReqVO) {
        return processListenerMapper.selectPage(pageReqVO);
    }

}