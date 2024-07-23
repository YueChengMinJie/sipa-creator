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
@Schema(description = "PrintRecord实体")
public class PrintRecord extends BasePo {
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用id")
    private Long applicationId;

    @Schema(description = "模板id")
    private Long templateId;

    @Schema(description = "模板code")
    private String templateCode;

    @Schema(description = "打印机id")
    private Long printerId;

    @Schema(description = "打印机code")
    private String printerCode;
}
