package com.sipa.tcp.creator.mbp;

import static com.sipa.tcp.creator.constant.CreatorConstant.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.sipa.tcp.creator.strategy.*;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConfig {
    private List<OutputFileInfo> outputFiles;

    private EntityStrategy entityStrategy = new EntityStrategy();

    private MapperStrategy mapperStrategy = new MapperStrategy();

    private MapperXmlStrategy mapperXmlStrategy = new MapperXmlStrategy();

    private ServiceStrategy serviceStrategy = new ServiceStrategy();

    private ServiceImplStrategy serviceImplStrategy = new ServiceImplStrategy();

    private ControllerStrategy controllerStrategy = new ControllerStrategy();

    @JsonIgnore
    public OutputFileInfo getEntityInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_ENTITY.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getMapperInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_MAPPER.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getMapperXmlInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_MAPPER_XML.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getServiceInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_SERVICE.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getServiceImplInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_SERVICE_IMPL.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getControllerInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_CONTROLLER.equals(f.getFileType()))).findFirst().get();
    }

    /**
     * 从另一个项目配置中合并可修改的配置项
     */
    public void merge(UserConfig sourceUserConfig, String sourceProjectConfigPath, String targetProjectConfigPath) {
        this.controllerStrategy = sourceUserConfig.getControllerStrategy();
        this.entityStrategy = sourceUserConfig.getEntityStrategy();
        this.mapperStrategy = sourceUserConfig.getMapperStrategy();
        this.mapperXmlStrategy = sourceUserConfig.getMapperXmlStrategy();
        this.serviceStrategy = sourceUserConfig.getServiceStrategy();
        this.serviceImplStrategy = sourceUserConfig.getServiceImplStrategy();
        changeTplPath(sourceUserConfig.getEntityInfo(), this.getEntityInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
        changeTplPath(sourceUserConfig.getMapperInfo(), this.getMapperInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
        changeTplPath(sourceUserConfig.getMapperXmlInfo(), this.getMapperXmlInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
        changeTplPath(sourceUserConfig.getServiceInfo(), this.getServiceInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
        changeTplPath(sourceUserConfig.getServiceImplInfo(), this.getServiceImplInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
        changeTplPath(sourceUserConfig.getControllerInfo(), this.getControllerInfo(), sourceProjectConfigPath,
            targetProjectConfigPath);
    }

    private void changeTplPath(OutputFileInfo source, OutputFileInfo dist, String sourceProjectConfigPath,
        String targetProjectConfigPath) {
        if (source == null || Strings.isNullOrEmpty(source.getTemplatePath())) {
            return;
        }
        dist.setTemplatePath(source.getTemplatePath().replace(sourceProjectConfigPath, targetProjectConfigPath));
    }
}
