package cn.newness.imip.module.bpm.controller.admin.definition.vo.model;

import cn.newness.imip.framework.common.validation.InEnum;
import cn.newness.imip.module.bpm.enums.definition.BpmModelFormTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Schema(description = "管理后台 - 流程模型的更新 Request VO")
@Data
public class BpmModelUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "编号不能为空")
    private String id;

    @Schema(description = "流程名称", example = "新奇")
    private String name;

    @Schema(description = "流程图标", example = "https://www.iocoder.cn/newness.jpg")
    @URL(message = "流程图标格式不正确")
    private String icon;

    @Schema(description = "流程描述", example = "我是描述")
    private String description;

    @Schema(description = "流程分类", example = "1")
    private String category;

    @Schema(description = "BPMN XML", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bpmnXml;

    @Schema(description = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
    @InEnum(BpmModelFormTypeEnum.class)
    private Integer formType;
    @Schema(description = "表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", example = "1024")
    private Long formId;
    @Schema(description = "自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/create")
    private String formCustomCreatePath;
    @Schema(description = "自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
            example = "/bpm/oa/leave/view")
    private String formCustomViewPath;

}
