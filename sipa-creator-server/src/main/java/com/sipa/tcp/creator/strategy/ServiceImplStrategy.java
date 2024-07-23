package com.sipa.tcp.creator.strategy;

import com.baomidou.mybatisplus.generator.config.ConstVal;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class ServiceImplStrategy {
    /**
     * 自定义继承的ServiceImpl类全称, 带包名
     */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
}
