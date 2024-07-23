package com.sipa.tcp.creator.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.mbp.OutputFileInfo;
import com.sipa.tcp.creator.mbp.UserConfig;
import com.sipa.tcp.creator.mbp.UserConfigStore;
import com.sipa.tcp.creator.strategy.*;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Service
@AllArgsConstructor
public class OutputFileInfoService {
    private final UserConfigStore userConfigStore;

    public void deleteOutputFileInfo(OutputFileInfo fileInfo) {
        if (fileInfo.isBuiltIn()) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.BUILT_IN_CONFIGURATION_CANNOT_BE_DELETED);
        }
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        fileInfos.remove(fileInfo);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveOutputFileInfo(OutputFileInfo saveFileInfo) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        // 替换原来的配置
        if (saveFileInfo.isBuiltIn()) {
            Collections.replaceAll(fileInfos, saveFileInfo, saveFileInfo);
        } else if (fileInfos.contains(saveFileInfo)) {
            Collections.replaceAll(fileInfos, saveFileInfo, saveFileInfo);
        } else {
            fileInfos.add(saveFileInfo);
        }
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveEntityStrategy(EntityStrategy entityStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setEntityStrategy(entityStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveMapperXmlStrategy(MapperXmlStrategy mapperXmlStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setMapperXmlStrategy(mapperXmlStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveMapperStrategy(MapperStrategy mapperStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setMapperStrategy(mapperStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveControllerStrategy(ControllerStrategy controllerStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setControllerStrategy(controllerStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveServiceStrategy(ServiceStrategy serviceStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setServiceStrategy(serviceStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveServiceImplStrategy(ServiceImplStrategy serviceImplStrategy) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setServiceImplStrategy(serviceImplStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public String getOutputPkgByFileType(String fileType) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        for (OutputFileInfo fileInfo : fileInfos) {
            if (fileInfo.getFileType().equals(fileType)) {
                return fileInfo.getOutputPackage();
            }
        }
        return null;
    }
}
