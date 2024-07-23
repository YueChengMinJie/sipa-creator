package com.sipa.tcp.creator.entity;

import com.sipa.tcp.cloud.core.pojo.po.BasePo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author caszhou
 * @date 2023-06-14
 */
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Message实体")
public class Message extends BasePo {
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private Integer msgTypeId;

    @Schema(description = "")
    private String title;

    @Schema(description = "")
    private String content;

    @Schema(description = "")
    private Long applicationId;
}
