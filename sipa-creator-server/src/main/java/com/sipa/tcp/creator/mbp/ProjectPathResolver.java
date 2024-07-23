package com.sipa.tcp.creator.mbp;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.constant.CreatorConstant;
import com.sipa.tcp.creator.property.GeneratorConfigProperty;
import com.sipa.tcp.creator.util.PathUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Tuple;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Slf4j
@Getter
@Component
public class ProjectPathResolver {
    private final Pattern packagePattern = Pattern.compile("[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*");

    private final String basePackage;

    private final String baseProjectPath;

    public ProjectPathResolver(GeneratorConfigProperty property) {
        this.basePackage = property.getBasePackage();
        this.baseProjectPath = PathUtil.getDefaultPath();
    }

    /**
     * 中文文件夹UTF8编码
     */
    public String getUtf8String(String basePath) {
        return URLDecoder.decode(basePath, StandardCharsets.UTF_8);
    }

    /**
     * 将文件输出的包名转换为绝对路径
     */
    public String convertPackageToPath(String packageName, String sourcePath, String resourcePath) {
        if (Strings.isNullOrEmpty(packageName)) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.PACKAGE_IS_EMPTY);
        }

        boolean isResourceFile = false;
        if (packageName.startsWith(CreatorConstant.PACKAGE_RESOURCES_PREFIX)) {
            packageName = packageName.replaceFirst(CreatorConstant.PACKAGE_RESOURCES_PREFIX, "");
            isResourceFile = true;
        } else if (packageName.startsWith(CreatorConstant.PACKAGE_JAVA_PREFIX)) {
            packageName = packageName.replaceFirst(CreatorConstant.PACKAGE_JAVA_PREFIX, "");
        }
        if (!packagePattern.matcher(packageName).matches()) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.NOT_VALID_PACKAGE_NAME, packageName);
        }

        String[] folders = packageName.split("\\.");
        StringBuilder path;
        if (isResourceFile) {
            path = new StringBuilder(resourcePath);
        } else {
            path = new StringBuilder(sourcePath);
        }
        for (String folder : folders) {
            path.append(File.separator).append(folder);
        }
        return path.toString();
    }

    public String convertPathToPackage(String path, String sourcePath, String resourcePath) {
        if (path.startsWith(sourcePath)) {
            path = path.replace(sourcePath, "");
        } else if (path.startsWith(resourcePath)) {
            path = path.replace(resourcePath, "");
        } else {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.CANNOT_CONVERT_TO_PACKAGE_NAME, path);
        }
        String packageStr = path.replace(File.separator, ".");
        if (packageStr.startsWith(".")) {
            packageStr = packageStr.substring(1);
        }
        return packageStr;
    }

    public String resolveEntityPackage() {
        return PathUtil.joinPackage(basePackage, "entity");
    }

    public String resolveControllerPackage() {
        return PathUtil.joinPackage(basePackage, "web");
    }

    public String resolveServicePackage() {
        return PathUtil.joinPackage(basePackage, "service");
    }

    public String resolveServiceImplPackage() {
        return PathUtil.joinPackage(basePackage, "service", "impl");
    }

    public String resolveMapperPackage() {
        return PathUtil.joinPackage(basePackage, "mapper");
    }

    public String resolveMapperXmlPackage() {
        return CreatorConstant.PACKAGE_RESOURCES_PREFIX + "mapper";
    }

    public Tuple refreshBaseProjectPath(boolean create) {
        String basePath = this.baseProjectPath + File.separator + System.currentTimeMillis();
        if (create) {
            FileUtil.mkdir(basePath);
        }
        String sourcePath =
            new File(basePath + File.separator + "src/main/java".replace("/", File.separator)).toString();
        String resourcePath =
            new File(basePath + File.separator + "src/main/resources".replace("/", File.separator)).toString();
        return new Tuple(basePath, sourcePath, resourcePath);
    }
}
