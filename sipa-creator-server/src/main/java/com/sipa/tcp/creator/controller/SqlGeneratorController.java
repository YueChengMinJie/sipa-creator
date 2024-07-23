package com.sipa.tcp.creator.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;

import com.sipa.tcp.creator.mbp.GenDtoFromSqlReq;
import com.sipa.tcp.creator.property.GeneratorConfigProperty;
import com.sipa.tcp.creator.service.SqlGeneratorService;
import com.sipa.tcp.creator.util.PathUtil;

import cn.hutool.core.lang.Tuple;
import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/sql")
public class SqlGeneratorController {
    private final SqlGeneratorService sqlGeneratorService;

    private final GeneratorConfigProperty generatorConfigProperty;

    @GetMapping("/base-package")
    public String getBasePackage() {
        return generatorConfigProperty.getBasePackage();
    }

    @PostMapping("/gen-mapper-method")
    public void genMapperMethodFromSql(@RequestBody GenDtoFromSqlReq data, HttpServletResponse response) {
        Tuple tuple = sqlGeneratorService.genMapperMethod(data);
        PathUtil.downloadZip(response, tuple);
    }
}
