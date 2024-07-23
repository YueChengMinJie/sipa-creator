package com.sipa.tcp.creator.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.constant.CreatorConstant;
import com.sipa.tcp.creator.enumeration.ElementPosition;
import com.sipa.tcp.creator.mbp.*;
import com.sipa.tcp.creator.property.GeneratorConfigProperty;
import com.sipa.tcp.creator.util.PathUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Tuple;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Slf4j
@Service
public class SqlGeneratorService {
    private final JdbcTemplate jdbcTemplate;

    private final GeneratorConfigProperty generatorConfigProperty;

    private final BeetlTemplateEngine beetlTemplateEngine;

    private final ProjectPathResolver projectPathResolver;

    private final Map<String, DataSourceConfig> configMap;

    private final MapperXmlParser mapperXmlParser;

    private final DynamicParamSqlEnhancer dynamicParamSqlEnhancer;

    private final JavaClassParser javaClassParser;

    private final Base64.Decoder decoder;

    private final List<String> rangeOperators;

    public SqlGeneratorService(JdbcTemplate jdbcTemplate, GeneratorConfigProperty generatorConfigProperty,
        BeetlTemplateEngine beetlTemplateEngine, ProjectPathResolver projectPathResolver,
        Map<String, DataSourceConfig> configMap, MapperXmlParser mapperXmlParser,
        DynamicParamSqlEnhancer dynamicParamSqlEnhancer, JavaClassParser javaClassParser) {
        this.jdbcTemplate = jdbcTemplate;
        this.generatorConfigProperty = generatorConfigProperty;
        this.beetlTemplateEngine = beetlTemplateEngine;
        this.projectPathResolver = projectPathResolver;
        this.configMap = configMap;
        this.mapperXmlParser = mapperXmlParser;
        this.dynamicParamSqlEnhancer = dynamicParamSqlEnhancer;
        this.javaClassParser = javaClassParser;
        this.decoder = Base64.getDecoder();
        this.rangeOperators = List.of("BETWEEN", "<", "<=", ">", ">=");
    }

    public Tuple genMapperMethod(GenDtoFromSqlReq data) {
        if (Strings.isNullOrEmpty(data.getSql())) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.DATA_SOURCE_SQL_CANNOT_BE_EMPTY);
        }

        String decodedSql = new String(decoder.decode(data.getSql()), StandardCharsets.UTF_8);
        if (!decodedSql.trim().toLowerCase().startsWith("select")) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.DTO_ONLY_FROM_SQL);
        }

        if (!Strings.isNullOrEmpty(data.getConfig().getFullPackage())) {
            try {
                Class.forName(data.getConfig().getFullPackage());
            } catch (Throwable t) {
                data.getConfig().setAutoCreatedResultDto(true);
            }
        }

        Tuple tuple = projectPathResolver.refreshBaseProjectPath(true);

        if (data.getConfig().isAutoCreatedResultDto()) {
            genDtoFileFromSql(data.getTenant(), decodedSql, data.getConfig(), tuple);
        }

        String namespace = genMapperElementsFromSql(decodedSql, data.getConfig(), tuple);
        // 在Dao中插入与Mapper节点对应的方法
        if (data.getConfig().getEnableCreateDaoMethod()) {
            if ("bean".equals(data.getConfig().getDaoMethodParamType())) {
                genParamDtoFromSql(decodedSql, data.getConfig().getDaoMethodParamDto(),
                    data.getConfig().isEnableLombok(), tuple);
            }
            addDaoMethod(namespace, decodedSql, data.getConfig(), tuple);
        }
        return tuple;
    }

    public void genDtoFileFromSql(String tenant, String sql, GenDtoConfig config, Tuple tuple) {
        sql = dynamicParamSqlEnhancer.clearIllegalStatements(sql);

        DynamicDataSourceContextHolder.push(tenant);
        SqlRowSet rowSet;
        try {
            rowSet = jdbcTemplate.queryForRowSet(sql);
        } catch (Exception e) {
            log.error("执行SQL发生错误", e);
            throw SystemExceptionFactory.bizException(ESystemErrorCode.ERROR_OCCURRED_EXECUTING_SQL);
        } finally {
            DynamicDataSourceContextHolder.clear();
        }

        SqlRowSetMetaData metaData = rowSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<DtoFieldInfo> fields = Lists.newArrayList();
        GlobalConfig.Builder gcb = new GlobalConfig.Builder();
        GlobalConfig gc = gcb.dateType(generatorConfigProperty.getDateType()).build();

        for (int i = 1; i <= columnCount; i++) {
            DtoFieldInfo resultField = new DtoFieldInfo();
            resultField.setColumnName(metaData.getColumnLabel(i));

            // 将数据库类型转换为java类型
            IColumnType columnType =
                configMap.get(tenant).getTypeConvert().processTypeConvert(gc, metaData.getColumnTypeName(i));
            resultField.setShortJavaType(columnType.getType());

            if (!Strings.isNullOrEmpty(columnType.getPkg())) {
                config.getImportPackages().add(columnType.getPkg());
            }

            resultField.setPropertyName(
                generatorConfigProperty.getNameConverter().propertyNameConvert(resultField.getColumnName()));
            fields.add(resultField);
        }

        config.setFields(fields);
        config.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (!Strings.isNullOrEmpty(config.getMapperLocation())) {
            config.setComment(config.getMapperLocation() + "的查询结果集, 该代码由sipa-creator自动生成");
        } else {
            config.setComment("该代码由sipa-creator自动生成");
        }

        genDtoFromConfig(config, tuple);
    }

    @SneakyThrows(Exception.class)
    private void genDtoFromConfig(GenDtoConfig config, Tuple tuple) {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);

        String outputPath =
            projectPathResolver.convertPackageToPath(config.getFullPackage(), tuple.get(1), tuple.get(2))
                + CreatorConstant.DOT_JAVA;
        createFile(outputPath);

        beetlTemplateEngine.writer(tplParams, "classpath:codetpls/dto.btl", new File(outputPath));
        log.info("DTO已成功生成, 输出位置为:" + outputPath);
    }

    @SneakyThrows(IOException.class)
    private void createFile(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public String genMapperElementsFromSql(String sql, GenDtoConfig config, Tuple tuple) {
        List<MapperElement> elements = Lists.newArrayList();
        // 如果DTO是自动生成的, 那么同时也生成结果映射集
        if (config.getResultMap() != null) {
            elements.add(createResultMapElement(config));
        }
        elements.add(createMapperMethodElement(sql, config));

        String mapperPath =
            projectPathResolver.convertPackageToPath(config.getMapperPackage(), tuple.get(1), tuple.get(2))
                + CreatorConstant.DOT_XML;
        createFile(mapperPath);
        beetlTemplateEngine.writer(Maps.newHashMap(), "classpath:codetpls/emptyMapper.xml.btl", new File(mapperPath));

        String namespace = mapperXmlParser.addElementInMapper(mapperPath, elements.toArray(new MapperElement[] {}));
        log.info("ResultMap和Mapper方法已生成, 输出位置为:" + mapperPath);
        return namespace;
    }

    public MapperElement createResultMapElement(GenDtoConfig config) {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);
        String resultMapStr = beetlTemplateEngine.write2String(tplParams, "classpath:codetpls/resultMap.btl");
        return MapperElement.builder()
            .id(config.getDtoName() + "Map")
            .comment("Author: " + config.getAuthor() + ", Date: " + DateUtil.format(new Date(), "yyyy-MM-dd") + ", "
                + config.getMapperElementId() + "的结果映射配置, 由sipa-creator自动生成")
            .content(resultMapStr)
            .location(ElementPosition.FIRST)
            .build();
    }

    public MapperElement createMapperMethodElement(String sql, GenDtoConfig config) {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);
        tplParams.put("elementType", "select");
        if (config.isEnableParseDynamicParams()) {
            sql = dynamicParamSqlEnhancer.enhanceDynamicConditions(sql);
        }
        tplParams.put("sql", sql);
        String methodEleStr = beetlTemplateEngine.write2String(tplParams, "classpath:codetpls/mapperMethods.btl");
        return MapperElement.builder()
            .id(config.getMapperElementId())
            .comment("Author: " + config.getAuthor() + ", Date: " + DateUtil.format(new Date(), "yyyy-MM-dd")
                + ", 由sipa-creator自动生成")
            .content(methodEleStr)
            .location(ElementPosition.LAST)
            .build();
    }

    public void genParamDtoFromSql(String sql, String paramDtoRef, boolean enableLombok, Tuple tuple) {
        List<ConditionExpr> conditionExprs = dynamicParamSqlEnhancer.parseSqlDynamicConditions(sql);
        if (conditionExprs.isEmpty()) {
            log.info("未检测到SQL的动态参数, 忽略参数DTO的生成");
            return;
        }
        List<DtoFieldInfo> fields = parseParamFieldsFromSql(sql);
        GenDtoConfig config = new GenDtoConfig();
        config.setEnableLombok(enableLombok);
        for (DtoFieldInfo fi : fields) {
            config.getImportPackages().addAll(fi.getImportJavaTypes());
        }
        config.setFullPackage(paramDtoRef);
        config.setFields(fields);
        config.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (!Strings.isNullOrEmpty(config.getMapperLocation())) {
            config.setComment(config.getMapperLocation() + "的查询参数Bean, 该代码由sipa-creator自动生成");
        } else {
            config.setComment("该代码由sipa-creator自动生成");
        }
        genDtoFromConfig(config, tuple);
    }

    /**
     * 根据相关配置生成DAO中的查询方法
     *
     * @param daoClassRef
     *            DAO的引用位置
     * @param sql
     *            查询SQL语句
     * @param config
     *            配置参数
     * @param tuple
     *            路径
     */
    public void addDaoMethod(String daoClassRef, String sql, GenDtoConfig config, Tuple tuple) {
        Set<String> imports = Sets.newHashSet();
        List<DtoFieldInfo> methodParams = Lists.newArrayList();

        String returnType;
        if (!Strings.isNullOrEmpty(config.getFullPackage())) {
            imports.add("java.util.List");
            imports.add(config.getFullPackage());
            returnType = "List<" + PathUtil.getShortNameFromFullRef(config.getFullPackage()) + ">";
        } else {
            imports.add("java.util.List");
            imports.add("java.util.Map");
            returnType = "List<Map<String,Object>>";
        }
        // 如果启用分页查询, 则修改相关的参数和返回值
        if (config.isEnablePageQuery()) {
            imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
            returnType = returnType.replaceFirst("List", "Page");
            DtoFieldInfo param = new DtoFieldInfo();
            param.setShortJavaType(PathUtil.getShortNameFromFullRef(returnType));
            param.setPropertyName("pageParam");
            methodParams.add(param);
        }
        List<DtoFieldInfo> sqlConditions = parseParamFieldsFromSql(sql);
        if (!sqlConditions.isEmpty()) {
            if ("map".equals(config.getDaoMethodParamType())) {
                DtoFieldInfo param = new DtoFieldInfo();
                param.setShortJavaType("Map<String,Object>");
                param.setPropertyName("params");
                param.addImportJavaType("java.util.Map");
                methodParams.add(param);
            } else if ("bean".equals(config.getDaoMethodParamType())) {
                DtoFieldInfo param = new DtoFieldInfo();
                param.setShortJavaType(PathUtil.getShortNameFromFullRef(config.getDaoMethodParamDto()));
                param.setPropertyName("params");
                param.addImportJavaType(config.getDaoMethodParamDto());
                methodParams.add(param);
            } else {
                for (DtoFieldInfo fieldInfo : sqlConditions) {
                    NodeList<AnnotationExpr> annotations = new NodeList<>();
                    AnnotationExpr paramAnno = new SingleMemberAnnotationExpr(new Name("Param"),
                        new StringLiteralExpr(fieldInfo.getPropertyName()));
                    annotations.add(paramAnno);
                    fieldInfo.setAnnotations(annotations);
                    fieldInfo.addImportJavaType("org.apache.ibatis.annotations.Param");
                }
                methodParams = sqlConditions;
            }
        }
        for (DtoFieldInfo feild : methodParams) {
            if (feild.getImportJavaTypes() != null) {
                imports.addAll(feild.getImportJavaTypes());
            }
        }

        JavaClassMethodInfo methodInfo = JavaClassMethodInfo.builder()
            .classRef(daoClassRef)
            .methodName(config.getMapperMethod())
            .returnType(returnType)
            .importJavaTypes(imports)
            .params(methodParams)
            .build();
        String sourcePath =
            projectPathResolver.convertPackageToPath(methodInfo.getClassRef(), tuple.get(1), tuple.get(2))
                + CreatorConstant.DOT_JAVA;
        createFile(sourcePath);
        beetlTemplateEngine.writer(Maps.newHashMap(), "classpath:codetpls/emptyMapper.java.btl", new File(sourcePath));
        javaClassParser.addMethod2Interface(methodInfo, sourcePath);
    }

    private List<DtoFieldInfo> parseParamFieldsFromSql(String sql) {
        List<ConditionExpr> conditionExprs = dynamicParamSqlEnhancer.parseSqlDynamicConditions(sql);
        if (conditionExprs.isEmpty()) {
            return Lists.newArrayList();
        }
        List<DtoFieldInfo> fields = Lists.newArrayList();
        for (ConditionExpr expr : conditionExprs) {
            for (String paramName : expr.getParamNames()) {
                DtoFieldInfo field = new DtoFieldInfo();
                field.setPropertyName(PathUtil.getShortNameFromFullRef(paramName));
                boolean isDate = paramName.toLowerCase().endsWith("date") || paramName.toLowerCase().endsWith("time");
                if ("IN".equalsIgnoreCase(expr.getOperator())) {
                    DbColumnType cType = DbColumnType.STRING;
                    if (isDate) {
                        cType = getRightDateType(generatorConfigProperty.getDateType());
                        field.addImportJavaType(cType.getPkg());
                    }
                    field.setShortJavaType("List<" + cType.getType() + ">");
                    field.addImportJavaType("java.util.List");
                } else if (rangeOperators.contains(expr.getOperator().toUpperCase())) {
                    DbColumnType cType = DbColumnType.LONG;
                    if (isDate) {
                        cType = getRightDateType(generatorConfigProperty.getDateType());
                        field.addImportJavaType(cType.getPkg());
                    }
                    field.setShortJavaType(cType.getType());
                } else {
                    field.setShortJavaType("String");
                }
                fields.add(field);
            }
        }
        return fields;
    }

    private DbColumnType getRightDateType(DateType dateType) {
        switch (dateType) {
            case ONLY_DATE:
                return DbColumnType.DATE;
            case SQL_PACK:
                return DbColumnType.DATE_SQL;
            case TIME_PACK:
                return DbColumnType.LOCAL_DATE;
            default:
                return null;
        }
    }
}
