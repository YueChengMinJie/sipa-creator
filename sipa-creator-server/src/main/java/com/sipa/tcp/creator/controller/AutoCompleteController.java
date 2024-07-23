package com.sipa.tcp.creator.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sipa.tcp.creator.service.AutoCompleteService;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ac")
public class AutoCompleteController {
    private final AutoCompleteService autoCompleteService;

    @GetMapping("/mapper-xml")
    public Set<String> getAllMapperXmlNames(String mapperLocationPrefix, String searchKey) {
        return autoCompleteService.searchXmlMapperName(mapperLocationPrefix, searchKey);
    }
}
