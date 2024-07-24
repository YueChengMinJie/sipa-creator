package com.sipa.tcp.creator.form;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@Getter
@Setter
@Schema(description = "打印模版")
public class PrintTemplateForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "号码")
    private String code;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "标签尺寸")
    private String size;

    @Schema(description = "打印机dpi")
    private String dpi;

    @Schema(description = "模版内容")
    private String content;
}
