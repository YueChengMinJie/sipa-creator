package com.sipa.tcp.creator.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.sipa.tcp.creator.mbp.TenantInfo;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TenantController {
    private final DynamicDataSourceProperties properties;

    @GetMapping("/tenants")
    public List<TenantInfo> getAllTables() {
        return properties.getDatasource()
            .keySet()
            .stream()
            .map(dataSourceProperty -> TenantInfo.builder().label(dataSourceProperty).value(dataSourceProperty).build())
            .collect(Collectors.toList());
    }
}
