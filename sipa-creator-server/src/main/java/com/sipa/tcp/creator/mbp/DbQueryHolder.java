package com.sipa.tcp.creator.mbp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import com.baomidou.mybatisplus.generator.config.querys.DbQueryRegistry;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Component
public class DbQueryHolder {
    private final DbQueryRegistry dbQueryRegistry;

    public DbQueryHolder() {
        this.dbQueryRegistry = new DbQueryRegistry();
    }

    public IDbQuery getDbQuery(DbType dbType) {
        return dbQueryRegistry.getDbQuery(dbType);
    }
}
