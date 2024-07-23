package com.sipa.tcp.creator.mbp;

import java.util.Set;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class DtoFieldInfo {
    private Set<String> importJavaTypes = Sets.newHashSet();

    private String columnName;

    private String shortJavaType;

    private String propertyName;

    private NodeList<AnnotationExpr> annotations;

    public String getGetMethodName() {
        String prefix = "get";
        if ("boolean".equalsIgnoreCase(shortJavaType)) {
            prefix = "is";
        }
        return prefix + StrUtil.upperFirst(propertyName);
    }

    public String getSetMethodName() {
        return "set" + StrUtil.upperFirst(propertyName);
    }

    public Boolean isConverted() {
        return columnName.equals(propertyName);
    }

    public void addImportJavaType(String type) {
        if (!Strings.isNullOrEmpty(type)) {
            importJavaTypes.add(type);
        }
    }
}
