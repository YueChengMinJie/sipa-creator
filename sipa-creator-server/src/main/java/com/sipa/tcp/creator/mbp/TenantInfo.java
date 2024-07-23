package com.sipa.tcp.creator.mbp;

import lombok.Builder;
import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@Builder
public class TenantInfo {
    private String label;

    private String value;
}
