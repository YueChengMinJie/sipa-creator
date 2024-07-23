package com.sipa.tcp.creator.mbp;

import com.sipa.tcp.creator.enumeration.ElementPosition;

import lombok.Builder;
import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@Builder
public class MapperElement {
    private String id;

    private String content;

    private String comment;

    private ElementPosition location;

    private boolean existed;
}
