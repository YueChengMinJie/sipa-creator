package com.sipa.tcp.creator.mbp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.constant.CreatorConstant;
import com.sipa.tcp.creator.util.PathUtil;
import com.sipa.tcp.creator.util.TemplateUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Slf4j
@Component
public class UserConfigStore implements InitializingBean {
    private String storeDir;

    private String userConfigPath;

    private final ProjectPathResolver pathResolver;

    public UserConfigStore(ProjectPathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    @Override
    public void afterPropertiesSet() {
        this.storeDir = PathUtil.joinPath(System.getProperty("user.home"), CreatorConstant.CONFIG_HOME);
        this.userConfigPath = this.storeDir + File.separator + "user-config.json";
    }

    public String getTemplateStoreDir() {
        return PathUtil.joinPath(this.storeDir, CreatorConstant.TEMPLATE_STORE_DIR);
    }

    public UserConfig getDefaultUserConfig() {
        UserConfig userConfig = getUserConfigFromFile();
        if (userConfig == null) {
            userConfig = new UserConfig();
            userConfig.setOutputFiles(getBuiltInFileInfo());
        }
        return userConfig;
    }

    public UserConfig getUserConfigFromFile() {
        if (!FileUtil.exist(this.userConfigPath)) {
            return null;
        }
        String userConfigStr = FileUtil.readString(userConfigPath, StandardCharsets.UTF_8);
        try {
            return JSONUtil.toBean(userConfigStr, UserConfig.class);
        } catch (Exception e) {
            log.error("读取用户配置文件发生错误: ", e);
            return null;
        }
    }

    @SneakyThrows(IOException.class)
    public void saveUserConfig(UserConfig userConfig) {
        if (userConfig == null) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.CANNOT_WRITE_EMPTY_USER_CONFIG);
        }
        String configStr = JSONUtil.toJsonStr(userConfig);
        File userConfigFile = new File(this.userConfigPath);
        if (userConfigFile.exists()) {
            userConfigFile.delete();
        }
        Files.createParentDirs(userConfigFile);
        userConfigFile.createNewFile();
        FileUtil.writeFromStream(new ByteArrayInputStream(configStr.getBytes(StandardCharsets.UTF_8)), userConfigFile);
    }

    public String uploadTemplate(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileSuffix = fileName.substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String saveFileName =
            fileName.substring(0, fileName.lastIndexOf(fileSuffix)) + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String savePath = PathUtil.joinPath(getTemplateStoreDir(), saveFileName);
        log.info("模板上传路径为: {}", savePath);
        File saveFile = new File(savePath);
        try {
            FileUtil.writeFromStream(file.getInputStream(), saveFile);
        } catch (IOException e) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.FAILED_TO_UPLOAD_TEMPLATE_FILE);
        }
        return CreatorConstant.RESOURCE_PREFIX_FILE + savePath;
    }

    public boolean checkUserConfigExisted() {
        return FileUtil.exist(this.storeDir);
    }

    public void importProjectConfig(String sourcePkg) {
        String configHomePath = PathUtil.joinPath(System.getProperty("user.home"), CreatorConstant.CONFIG_HOME);
        if (!FileUtil.exist(configHomePath)) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.CONFIGURATION_DIR_NOT_FOUND, configHomePath);
        }
        File[] files = FileUtil.ls(configHomePath);
        boolean flag = false;
        for (File file : files) {
            if (file.isDirectory() && file.getName().equals(sourcePkg)) {
                File projectConfigDir = new File(this.storeDir);
                FileUtil.copyContent(file, projectConfigDir, true);
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.CONFIGURATION_TO_IMPORT_WAS_NOT_FOUND);
        }
        String sourceProjectConfigPath =
            PathUtil.joinPath(System.getProperty("user.home"), CreatorConstant.CONFIG_HOME, sourcePkg);
        String targetProjectConfigPath = this.storeDir;
        UserConfig currentUserConfig = new UserConfig();
        currentUserConfig.setOutputFiles(getBuiltInFileInfo());
        currentUserConfig.merge(this.getUserConfigFromFile(), sourceProjectConfigPath, targetProjectConfigPath);
        this.saveUserConfig(currentUserConfig);
    }

    public List<String> getAllSavedProject() {
        String configHomePath = PathUtil.joinPath(System.getProperty("user.home"), CreatorConstant.CONFIG_HOME);
        if (!FileUtil.exist(configHomePath)) {
            return Collections.emptyList();
        }
        List<String> projects = Lists.newArrayList();
        File[] files = FileUtil.ls(configHomePath);
        for (File file : files) {
            if (file.isDirectory()) {
                projects.add(file.getName());
            }
        }
        return projects;
    }

    /**
     * 默认的内置输出文件的信息
     */
    private List<OutputFileInfo> getBuiltInFileInfo() {
        List<OutputFileInfo> builtInFiles = Lists.newArrayList();
        // Entity
        OutputFileInfo entityFile = new OutputFileInfo();
        entityFile.setBuiltIn(true);
        entityFile.setFileType(CreatorConstant.FILE_TYPE_ENTITY);
        entityFile.setOutputLocation(pathResolver.resolveEntityPackage());
        entityFile.setTemplateName(TemplateUtil.fileType2TemplateName(entityFile.getFileType()));
        builtInFiles.add(entityFile);
        // Mapper xml
        OutputFileInfo mapperXmlFile = new OutputFileInfo();
        mapperXmlFile.setBuiltIn(true);
        mapperXmlFile.setFileType(CreatorConstant.FILE_TYPE_MAPPER_XML);
        mapperXmlFile.setOutputLocation(pathResolver.resolveMapperXmlPackage());
        mapperXmlFile.setTemplateName(TemplateUtil.fileType2TemplateName(mapperXmlFile.getFileType()));
        builtInFiles.add(mapperXmlFile);
        // Mapper
        OutputFileInfo mapperFile = new OutputFileInfo();
        mapperFile.setBuiltIn(true);
        mapperFile.setFileType(CreatorConstant.FILE_TYPE_MAPPER);
        mapperFile.setOutputLocation(pathResolver.resolveMapperPackage());
        mapperFile.setTemplateName(TemplateUtil.fileType2TemplateName(mapperFile.getFileType()));
        builtInFiles.add(mapperFile);
        // Service
        OutputFileInfo serviceFile = new OutputFileInfo();
        serviceFile.setBuiltIn(true);
        serviceFile.setFileType(CreatorConstant.FILE_TYPE_SERVICE);
        serviceFile.setOutputLocation(pathResolver.resolveServicePackage());
        serviceFile.setTemplateName(TemplateUtil.fileType2TemplateName(serviceFile.getFileType()));
        builtInFiles.add(serviceFile);
        // Service Impl
        OutputFileInfo serviceImplFile = new OutputFileInfo();
        serviceImplFile.setBuiltIn(true);
        serviceImplFile.setFileType(CreatorConstant.FILE_TYPE_SERVICE_IMPL);
        serviceImplFile.setOutputLocation(pathResolver.resolveServiceImplPackage());
        serviceImplFile.setTemplateName(TemplateUtil.fileType2TemplateName(serviceImplFile.getFileType()));
        builtInFiles.add(serviceImplFile);
        // Api
        OutputFileInfo apiFile = new OutputFileInfo();
        apiFile.setBuiltIn(false);
        apiFile.setFileType(CreatorConstant.FILE_TYPE_API);
        apiFile.setOutputLocation(PathUtil.joinPackage(pathResolver.getBasePackage(), "api"));
        apiFile.setTemplateName(TemplateUtil.fileType2TemplateName(apiFile.getFileType()));
        apiFile.setTemplatePath(CreatorConstant.RESOURCE_PREFIX_CLASSPATH + "codetpls/api.java.btl");
        builtInFiles.add(apiFile);
        // Controller
        OutputFileInfo controllerFile = new OutputFileInfo();
        controllerFile.setBuiltIn(true);
        controllerFile.setFileType(CreatorConstant.FILE_TYPE_CONTROLLER);
        controllerFile.setOutputLocation(pathResolver.resolveControllerPackage());
        controllerFile.setTemplateName(TemplateUtil.fileType2TemplateName(controllerFile.getFileType()));
        builtInFiles.add(controllerFile);
        return builtInFiles;
    }
}
