package com.sipa.tcp.creator.mbp;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@Builder
public class JavaClassMethodInfo {
    private String classRef;

    private String methodName;

    private String returnType;

    private String comments;

    private List<DtoFieldInfo> params;

    private Set<String> importJavaTypes;
}
