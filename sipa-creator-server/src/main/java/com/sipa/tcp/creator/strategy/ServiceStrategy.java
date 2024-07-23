package com.sipa.tcp.creator.strategy;

import com.baomidou.mybatisplus.generator.config.ConstVal;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class ServiceStrategy {
    /**
     * 自定义继承的Service类全称, 带包名
     */
    private String superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
}
