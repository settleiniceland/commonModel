package cn.newness.imip.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

/**
 * @author machuran
 * @date 2024/7/16
 * @time 10:35
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataPermissionDO extends BaseDO{
    /**
    * 部门id
    *
    * @param
    * @author machuran
    * @date 2024/7/16
    * @Return
    */
    @TableField(fill = FieldFill.INSERT,jdbcType = JdbcType.BIGINT)
    private Long deptId;
}
