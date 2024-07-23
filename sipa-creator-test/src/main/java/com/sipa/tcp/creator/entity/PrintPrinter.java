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
@Schema(description = "PrintPrinter实体")
public class PrintPrinter extends BasePo {
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
