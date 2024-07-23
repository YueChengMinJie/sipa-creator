package com.sipa.tcp.creator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sipa.tcp.creator.mbp.TableInfo;
import com.sipa.tcp.creator.service.DatabaseService;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/db")
public class DatabaseController {
    private final DatabaseService databaseService;

    @GetMapping("/tables")
    public List<TableInfo> getAllTables(String tenant) {
        return databaseService.getTablesFromDb(tenant);
    }
}
