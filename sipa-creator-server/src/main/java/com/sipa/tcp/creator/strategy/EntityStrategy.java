package com.sipa.tcp.creator.strategy;

import java.util.List;

import com.sipa.boot.core.pojo.po.BasePo;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class EntityStrategy {
    /**
     * 自定义继承的Entity类全称, 带包名
     */
    private String superEntityClass = BasePo.class.getName();

    /**
     * 自定义基础的Entity类, 公共字段
     */
    private List<String> superEntityColumns = List.of("id", "status", "create_time", "create_by", "create_name",
        "create_company_id", "update_time", "update_by", "update_name", "is_deleted", "version");

    /**
     * 需要自动填充的字段, 格式为 字段名:填充时机 例如: col:update
     */
    private List<String> tableFills;

    /**
     * 乐观锁属性名称
     */
    private String versionFieldName;

    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName;

    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;

    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean entityColumnConstant = true;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord = false;

    /**
     * 【实体】是否为构建者模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     */
    private boolean entityBuilderModel = true;

    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean entityLombokModel = true;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = true;

    /**
     * 是否生成实体时, 生成字段注解
     */
    private boolean entityTableFieldAnnotationEnable = true;

    /**
     * 开启 swagger3 支持
     */
    private boolean swagger3 = true;
}
