package com.sipa.tcp.creator.strategy;

import com.baomidou.mybatisplus.generator.config.ConstVal;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class MapperStrategy {
    /**
     * 自定义继承的Mapper类全称, 带包名
     */
    private String superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
}
