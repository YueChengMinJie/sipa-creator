package com.sipa.tcp.creator.property;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.google.common.collect.Maps;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.mbp.NameConverter;
import com.sipa.tcp.creator.mbp.TemplateVariableInjector;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@ConfigurationProperties(prefix = GeneratorConfigProperty.PREFIX)
public class GeneratorConfigProperty {
    public static final String PREFIX = "sipa-creator";

    /**
     * 生成的文件所保存的包路径
     */
    private String basePackage = "com.sipa";

    /**
     * 数据库schema,PG_SQL,ORACLE,DB2, MSSQL类型的数据库需要指定
     */
    private String schemaName = "MYSQL";

    private DbType dbType = DbType.MYSQL;

    /**
     * 数据库时间类型与java class的对应策略
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 注入自定义模板参数
     */
    private TemplateVariableInjector templateVariableInjector = tableInfo -> {
        Map<String, Object> variables = Maps.newHashMap();
        if (StringUtils.isBlank(tableInfo.getComment())) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.TABLE_NEED_COMMENT);
        }
        variables.put("tableInfoComment", tableInfo.getComment());
        return variables;
    };

    /**
     * 表前缀, 如果设置后会在生成entity名称时去掉该后缀
     */
    private String tablePrefix;

    /**
     * 自定义名称转换规则
     */
    private NameConverter nameConverter = new NameConverter() {
        @Override
        public String serviceNameConvert(String entityName) {
            return entityName + "Service";
        }
    };

    /**
     * 自定义数据字段类型和实体类型映射
     */
    private ITypeConvert typeConvert;

    /**
     * 全局指定数据表中ID的生成模式, 影响自动生成的Entity中ID字段的注解
     */
    private IdType idType;
}
