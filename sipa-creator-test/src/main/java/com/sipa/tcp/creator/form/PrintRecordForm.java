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
@Schema(description = "打印记录")
public class PrintRecordForm implements Serializable {
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
