package com.sipa.tcp.creator.entity;

import com.sipa.boot.core.pojo.po.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author caszhou
 * @date 2024-07-23
 */
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "PrintTemplate实体")
public class PrintTemplate extends BasePo {
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
