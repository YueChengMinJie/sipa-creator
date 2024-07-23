package com.sipa.tcp.creator.mbp;

import java.util.Map;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;

/**
 * @author caszhou
 * @date 2022/6/10
 */
public interface TemplateVariableInjector {
    Map<String, Object> getCustomTemplateVariables(TableInfo tableInfo);
}
