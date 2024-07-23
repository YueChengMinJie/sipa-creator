package com.sipa.tcp.creator.mbp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.sipa.tcp.creator.constant.CreatorConstant;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFileInfo {
    private String fileType;

    /**
     * 文件的输出包名
     */
    private String outputLocation;

    private String templateName;

    private String templatePath;

    private boolean builtIn;

    public String getOutputPackage() {
        if (Strings.isNullOrEmpty(outputLocation)) {
            return "";
        }
        if (outputLocation.startsWith(CreatorConstant.PACKAGE_RESOURCES_PREFIX)) {
            return outputLocation.replaceFirst(CreatorConstant.PACKAGE_RESOURCES_PREFIX, "");
        } else if (outputLocation.startsWith(CreatorConstant.PACKAGE_JAVA_PREFIX)) {
            return outputLocation.replaceFirst(CreatorConstant.PACKAGE_JAVA_PREFIX, "");
        }
        return outputLocation;
    }

    @JsonIgnore
    public String getAvailableTemplatePath() {
        if (!Strings.isNullOrEmpty(templatePath)) {
            return templatePath;
        }
        switch (fileType) {
            case CreatorConstant.FILE_TYPE_ENTITY:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/entity.java.btl";
            case CreatorConstant.FILE_TYPE_MAPPER:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/mapper.java.btl";
            case CreatorConstant.FILE_TYPE_MAPPER_XML:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/mapper.xml.btl";
            case CreatorConstant.FILE_TYPE_SERVICE:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/service.java.btl";
            case CreatorConstant.FILE_TYPE_SERVICE_IMPL:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/serviceImpl.java.btl";
            case CreatorConstant.FILE_TYPE_CONTROLLER:
                return CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/controller.java.btl";
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OutputFileInfo) {
            OutputFileInfo file = (OutputFileInfo)obj;
            if (file.getFileType() == null || this.getFileType() == null) {
                return false;
            }
            return file.getFileType().equalsIgnoreCase(this.getFileType());
        }
        return false;
    }
}
