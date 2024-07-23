package com.sipa.tcp.creator.util;

import java.io.InputStream;

import com.sipa.tcp.creator.constant.CreatorConstant;

/**
 * @author caszhou
 * @date 2022/6/10
 */
public class TemplateUtil {
    public static InputStream getBuiltInTemplate(String fileType) {
        return TemplateUtil.class.getResourceAsStream("/codetpls/" + fileType2TemplateName(fileType));
    }

    public static String fileType2TemplateName(String fileType) {
        if (fileType.equalsIgnoreCase(CreatorConstant.FILE_TYPE_MAPPER_XML)
            || fileType.equalsIgnoreCase(CreatorConstant.FILE_TYPE_MAPPER)) {
            return fileType.toLowerCase() + ".btl";
        }
        if (fileType.equals(CreatorConstant.FILE_TYPE_SERVICE_IMPL)) {
            return "serviceImpl.java.btl";
        }
        return fileType.toLowerCase() + ".java.btl";
    }
}
