package com.sipa.tcp.creator.vo;

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
@Schema(description = "打印机")
public class PrintPrinterVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "端口")
    private String port;

    @Schema(description = "品牌")
    private String brand;
}
