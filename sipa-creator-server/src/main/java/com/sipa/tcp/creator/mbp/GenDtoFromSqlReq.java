package com.sipa.tcp.creator.mbp;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class GenDtoFromSqlReq {
    private String tenant;

    private String sql;

    private GenDtoConfig config = new GenDtoConfig();
}
