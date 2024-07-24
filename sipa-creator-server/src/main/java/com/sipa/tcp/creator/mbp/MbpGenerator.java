package com.sipa.tcp.creator.mbp;

import java.io.Serializable;
import java.util.*;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.google.common.collect.Maps;
import com.sipa.tcp.creator.constant.CreatorConstant;
import com.sipa.tcp.creator.property.GeneratorConfigProperty;
import com.sipa.tcp.creator.strategy.*;
import com.sipa.tcp.creator.util.PathUtil;

import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Slf4j
@Component
@AllArgsConstructor
public class MbpGenerator {
    private final GeneratorConfigProperty generatorConfigProperty;

    private final Map<String, DataSourceConfig> configMap;

    private final UserConfigStore userConfigStore;

    private final ProjectPathResolver projectPathResolver;

    private final BeetlTemplateEngine beetlTemplateEngine;

    /**
     * 根据所选择的配置生成CRUD代码
     */
    public Tuple genCodeBatch(String tenant, GenSetting genSetting, List<String> tables) {
        DataSourceConfig dataSourceConfig = configMap.get(tenant);

        Tuple tuple = projectPathResolver.refreshBaseProjectPath(true);

        // 生成策略配置
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        FastAutoGenerator
            .create(dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword())
            .dataSourceConfig(builder -> {
                builder.schema(generatorConfigProperty.getSchemaName());
                builder.typeConvert(generatorConfigProperty.getTypeConvert());
            })
            .globalConfig(builder -> {
                builder.dateType(generatorConfigProperty.getDateType());
                // 指定所有生成文件的根目录
                builder.outputDir(tuple.get(1));
                builder.author(StringUtils.hasText(genSetting.getAuthor()) ? genSetting.getAuthor()
                    : System.getProperty("user.name"));
                if (userConfig.getEntityStrategy().isSwagger3()) {
                    builder.enableSpringdoc();
                }
                builder.disableOpenDir();
            })
            .templateEngine(beetlTemplateEngine)
            .packageConfig(builder -> configPackage(builder, genSetting.getModuleName(), userConfig, tuple))
            .templateConfig(builder -> configTemplate(builder, genSetting.getChoosedOutputFiles(), userConfig))
            .injectionConfig(builder -> configInjection(builder, userConfig, genSetting, tenant))
            .strategyConfig(builder -> {
                builder.addInclude(String.join(",", tables)).disableSqlFilter().enableSkipView();

                configEntity(builder.entityBuilder(), userConfig.getEntityStrategy(), genSetting.isOverride());
                configMapper(builder.mapperBuilder(), userConfig.getMapperStrategy(), userConfig.getMapperXmlStrategy(),
                    genSetting.isOverride());
                configService(builder.serviceBuilder(), userConfig.getServiceStrategy(),
                    userConfig.getServiceImplStrategy(), genSetting.isOverride());
                configController(builder.controllerBuilder(), userConfig.getControllerStrategy(),
                    genSetting.isOverride());
            })
            .execute();
        return tuple;
    }

    private void configPackage(PackageConfig.Builder builder, String moduleName, UserConfig userConfig, Tuple tuple) {
        String mapperXmlOutputPath = getOutputPathByFileType(CreatorConstant.FILE_TYPE_MAPPER_XML, userConfig, tuple);
        // 这里的模块名处理方式和原版的MPG不同, 是将模块名放在包名最后
        String entityPkg = PathUtil.joinModulePackage(userConfig.getEntityInfo().getOutputPackage(), moduleName);
        String mapperPkg = PathUtil.joinModulePackage(userConfig.getMapperInfo().getOutputPackage(), moduleName);
        String servicePkg = PathUtil.joinModulePackage(userConfig.getServiceInfo().getOutputPackage(), moduleName);
        String serviceImplPkg =
            PathUtil.joinModulePackage(userConfig.getServiceImplInfo().getOutputPackage(), moduleName);
        String controllerPkg =
            PathUtil.joinModulePackage(userConfig.getControllerInfo().getOutputPackage(), moduleName);
        // 子包名已经包含了完整路径
        builder.parent("")
            .moduleName("")
            .entity(entityPkg)
            .controller(controllerPkg)
            .mapper(mapperPkg)
            .service(servicePkg)
            .serviceImpl(serviceImplPkg)
            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlOutputPath));
    }

    private void configTemplate(TemplateConfig.Builder builder, List<String> choosedFileTypes, UserConfig userConfig) {
        builder.entity(findTemplatePath(CreatorConstant.FILE_TYPE_ENTITY, userConfig));
        builder.mapper(findTemplatePath(CreatorConstant.FILE_TYPE_MAPPER, userConfig));
        builder.xml(findTemplatePath(CreatorConstant.FILE_TYPE_MAPPER_XML, userConfig));
        builder.service(findTemplatePath(CreatorConstant.FILE_TYPE_SERVICE, userConfig));
        builder.serviceImpl(findTemplatePath(CreatorConstant.FILE_TYPE_SERVICE_IMPL, userConfig));
        builder.controller(findTemplatePath(CreatorConstant.FILE_TYPE_CONTROLLER, userConfig));
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_ENTITY)) {
            builder.disable(TemplateType.ENTITY);
        }
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_MAPPER)) {
            builder.disable(TemplateType.MAPPER);
        }
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_MAPPER_XML)) {
            builder.disable(TemplateType.XML);
        }
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_SERVICE)) {
            builder.disable(TemplateType.SERVICE);
        }
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_SERVICE_IMPL)) {
            builder.disable(TemplateType.SERVICE_IMPL);
        }
        if (!choosedFileTypes.contains(CreatorConstant.FILE_TYPE_CONTROLLER)) {
            builder.disable(TemplateType.CONTROLLER);
        }
    }

    // 自定义模板参数配置
    private void configInjection(InjectionConfig.Builder builder, UserConfig userConfig, GenSetting genSetting,
        String tenant) {
        // 自定义参数
        builder.beforeOutputFile((tableInfo, objectMap) -> {
            TemplateVariableInjector tvi = generatorConfigProperty.getTemplateVariableInjector();
            Map<String, Object> vars = null;
            if (tvi != null) {
                vars = tvi.getCustomTemplateVariables(tableInfo);
            }
            if (vars == null) {
                vars = Maps.newHashMap();
            }

            // 用于控制controller中对应API是否展示的自定义参数
            Map<String, Object> controllerMethodsVar = Maps.newHashMap();
            for (String method : genSetting.getChoosedControllerMethods()) {
                controllerMethodsVar.put(method, true);
            }
            if (!controllerMethodsVar.isEmpty()) {
                controllerMethodsVar.put("hasMethod", true);
            }
            vars.put("controllerMethods", controllerMethodsVar);
            if (!StrUtil.isEmpty(generatorConfigProperty.getSchemaName())) {
                vars.put("schemaName", generatorConfigProperty.getSchemaName() + ".");
            }
            vars.put("tenant", tenant);
            // 目录
            Map<String, Object> o1 = (Map)objectMap.get("package");
            String o2 = (String)o1.get("Entity");
            vars.put("packageParent", o2.substring(0, o2.indexOf(".entity")));
            objectMap.putAll(vars);

            // 删除
            // 0 = "com.baomidou.mybatisplus.annotation.TableField"
            // 1 = "com.baomidou.mybatisplus.annotation.TableName"
            // 3 = "java.io.Serializable"
            tableInfo.getImportPackages()
                .removeIf(
                    s -> Set
                        .of("com.baomidou.mybatisplus.annotation.TableField", TableName.class.getName(),
                            Serializable.class.getName())
                        .contains(s));
        });
        // 自定义文件生成
        for (OutputFileInfo outputFileInfo : userConfig.getOutputFiles()) {
            if (!outputFileInfo.isBuiltIn()
                && genSetting.getChoosedOutputFiles().contains(outputFileInfo.getFileType())) {
                CustomFile.Builder fileBuilder = new CustomFile.Builder();
                // 注意这里传入的是fileType,配合自定义的TemplateEngine.outputCustomFile生成自定义文件
                fileBuilder.fileName(outputFileInfo.getFileType());
                fileBuilder.templatePath(outputFileInfo.getTemplatePath());
                fileBuilder.packageName(
                    PathUtil.joinModulePackage(outputFileInfo.getOutputPackage(), genSetting.getModuleName()));
                if (genSetting.isOverride()) {
                    fileBuilder.enableFileOverride();
                }
                builder.customFile(fileBuilder.build());
            }
        }
    }

    /**
     * 配置entity的生成信息
     */
    private void configEntity(Entity.Builder entityBuilder, EntityStrategy entityStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfigProperty.getNameConverter();
        entityBuilder.idType(generatorConfigProperty.getIdType());
        entityBuilder.nameConvert(new INameConvert() {
            @Override
            public String entityNameConvert(TableInfo tableInfo) {
                return nameConverter.entityNameConvert(tableInfo.getName(), generatorConfigProperty.getTablePrefix());
            }

            @Override
            public String propertyNameConvert(TableField field) {
                return nameConverter.propertyNameConvert(field.getName());
            }
        });
        entityBuilder.superClass(entityStrategy.getSuperEntityClass());
        if (fileOverride) {
            entityBuilder.enableFileOverride();
        }
        if (!entityStrategy.isEntitySerialVersionUID()) {
            entityBuilder.disableSerialVersionUID();
        }
        if (entityStrategy.isEntityBuilderModel()) {
            entityBuilder.enableChainModel();
        }
        if (entityStrategy.isEntityLombokModel()) {
            entityBuilder.enableLombok();
        }
        if (entityStrategy.isEntityBooleanColumnRemoveIsPrefix()) {
            entityBuilder.enableRemoveIsPrefix();
        }
        if (entityStrategy.isEntityTableFieldAnnotationEnable()) {
            entityBuilder.enableTableFieldAnnotation();
        }
        if (entityStrategy.isActiveRecord()) {
            entityBuilder.enableActiveRecord();
        }
        if (!StrUtil.isEmpty(entityStrategy.getVersionFieldName())) {
            entityBuilder.versionColumnName(entityStrategy.getVersionFieldName());
            entityBuilder.versionPropertyName(entityStrategy.getVersionFieldName());
        }
        if (!StrUtil.isEmpty(entityStrategy.getLogicDeleteFieldName())) {
            entityBuilder.logicDeleteColumnName(entityStrategy.getLogicDeleteFieldName());
            entityBuilder.logicDeletePropertyName(entityStrategy.getLogicDeleteFieldName());
        }
        if (entityStrategy.getSuperEntityColumns() != null) {
            entityBuilder.addSuperEntityColumns(entityStrategy.getSuperEntityColumns());
        }
        if (entityStrategy.getTableFills() != null && !entityStrategy.getTableFills().isEmpty()) {
            List<IFill> tableFills = new ArrayList<>();
            for (String tableFillStr : entityStrategy.getTableFills()) {
                if (!StrUtil.isEmpty(tableFillStr)) {
                    String[] tmp = tableFillStr.split(":");
                    IFill tableFill = new Column(tmp[0], FieldFill.valueOf(tmp[1].toUpperCase()));
                    tableFills.add(tableFill);
                }
            }
            entityBuilder.addTableFills(tableFills);
        }
        entityBuilder.idType(generatorConfigProperty.getIdType());
        entityBuilder.convertFileName(entityName -> entityName);
    }

    /**
     * 配置mapper和mapper-xml
     */
    private void configMapper(Mapper.Builder mapperBuilder, MapperStrategy mapperStrategy,
        MapperXmlStrategy mapperXmlStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfigProperty.getNameConverter();
        if (mapperStrategy.getSuperMapperClass() != null) {
            mapperBuilder.superClass(mapperStrategy.getSuperMapperClass());
        }
        if (mapperXmlStrategy.isBaseResultMap()) {
            mapperBuilder.enableBaseResultMap();
            // TODO:enableBaseColumnList, cache目前没有页面配置
            mapperBuilder.enableBaseColumnList();
        }
        mapperBuilder.convertMapperFileName(nameConverter::mapperNameConvert);
        mapperBuilder.convertXmlFileName(nameConverter::mapperXmlNameConvert);
        if (fileOverride) {
            mapperBuilder.enableFileOverride();
        }
    }

    /**
     * 配置service
     */
    private void configService(Service.Builder serviceBuilder, ServiceStrategy serviceStrategy,
        ServiceImplStrategy serviceImplStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfigProperty.getNameConverter();
        if (fileOverride) {
            serviceBuilder.enableFileOverride();
        }
        if (serviceStrategy.getSuperServiceClass() != null) {
            serviceBuilder.superServiceClass(serviceStrategy.getSuperServiceClass());
        }
        if (serviceImplStrategy.getSuperServiceImplClass() != null) {
            serviceBuilder.superServiceImplClass(serviceImplStrategy.getSuperServiceImplClass());
        }
        serviceBuilder.convertServiceFileName(nameConverter::serviceNameConvert);
        serviceBuilder.convertServiceImplFileName(nameConverter::serviceImplNameConvert);
    }

    /**
     * 配置Controller
     */
    private void configController(Controller.Builder controllerBuilder, ControllerStrategy controllerStrategy,
        boolean fileOverride) {
        NameConverter nameConverter = generatorConfigProperty.getNameConverter();
        if (fileOverride) {
            controllerBuilder.enableFileOverride();
        }
        if (controllerStrategy.isRestControllerStyle()) {
            controllerBuilder.enableRestStyle();
        }
        if (controllerStrategy.isControllerMappingHyphenStyle()) {
            controllerBuilder.enableHyphenStyle();
        }
        if (controllerStrategy.getSuperControllerClass() != null) {
            controllerBuilder.superClass(controllerStrategy.getSuperControllerClass());
        }
        controllerBuilder.convertFileName(nameConverter::controllerNameConvert);
    }

    private OutputFileInfo findFileConfigByType(String fileType, UserConfig userConfig) {
        for (OutputFileInfo outputFileInfo : userConfig.getOutputFiles()) {
            if (fileType.equals(outputFileInfo.getFileType())) {
                return outputFileInfo;
            }
        }
        return null;
    }

    private String getOutputPathByFileType(String fileType, UserConfig userConfig, Tuple tuple) {
        return projectPathResolver.convertPackageToPath(
            Objects.requireNonNull(findFileConfigByType(fileType, userConfig)).getOutputLocation(), tuple.get(1),
            tuple.get(2)

        );
    }

    private String findTemplatePath(String fileType, UserConfig userConfig) {
        return Objects.requireNonNull(findFileConfigByType(fileType, userConfig)).getAvailableTemplatePath();
    }
}
