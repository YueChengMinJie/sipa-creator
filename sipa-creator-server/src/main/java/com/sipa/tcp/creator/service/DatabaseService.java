package com.sipa.tcp.creator.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import com.google.common.collect.Lists;
import com.sipa.tcp.creator.mbp.DbQueryHolder;
import com.sipa.tcp.creator.mbp.TableInfo;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Service
@AllArgsConstructor
public class DatabaseService {
    private final JdbcTemplate jdbcTemplate;

    private final DbQueryHolder dbQueryHolder;

    private final DynamicDataSourceProperties properties;

    private final Map<String, DataSourceConfig> configMap;

    public List<TableInfo> getTablesFromDb(String tenant) {
        if (StringUtils.isBlank(tenant)) {
            tenant = properties.getDatasource().keySet().iterator().next();
        }
        IDbQuery dbQuery = dbQueryHolder.getDbQuery(configMap.get(tenant).getDbType());
        List<TableInfo> tableInfos = Lists.newArrayList();
        DynamicDataSourceContextHolder.push(tenant);
        List<Map<String, Object>> results;
        try {
            results = jdbcTemplate.queryForList(getTableSql(tenant));
            for (Map<String, Object> table : results) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setName((String)table.get(dbQuery.tableName()));
                tableInfo.setComment((String)table.get(dbQuery.tableComment()));
                tableInfos.add(tableInfo);
            }
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
        return tableInfos;
    }

    public String getTableSql(String tenant) {
        String tablesSql = dbQueryHolder.getDbQuery(configMap.get(tenant).getDbType()).tablesSql();
        String schema = configMap.get(tenant).getSchemaName();
        if (schema == null) {
            schema = getDefaultSchema(tenant);
        }
        tablesSql = String.format(tablesSql, schema);
        return tablesSql;
    }

    private String getDefaultSchema(String tenant) {
        String schema = null;
        DbType dbType = configMap.get(tenant).getDbType();
        if (DbType.POSTGRE_SQL == dbType) {
            // pg 默认 schema=public
            schema = "public";
        } else if (DbType.KINGBASE_ES == dbType) {
            // kingbase 默认 schema=PUBLIC
            schema = "PUBLIC";
        } else if (DbType.DB2 == dbType) {
            // db2 默认 schema=current schema
            schema = "current schema";
        } else if (DbType.ORACLE == dbType) {
            // oracle 默认 schema=username
            schema = Objects.requireNonNull(configMap.get(tenant).getUsername()).toUpperCase();
        } else if (DbType.SQL_SERVER == dbType) {
            // SQL_SERVER 2005以上 默认 schema=dbo
            schema = "dbo";
        }
        return schema;
    }
}
