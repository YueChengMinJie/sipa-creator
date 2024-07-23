package com.sipa.tcp.creator.strategy;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class MapperXmlStrategy {
    /**
     * 是否生成baseResultMap
     */
    private boolean baseResultMap = false;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;
}
