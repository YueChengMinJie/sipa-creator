package com.sipa.tcp.creator.service;

import java.io.File;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.tcp.creator.constant.CreatorConstant;
import com.sipa.tcp.creator.mbp.ProjectPathResolver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Tuple;
import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Service
@AllArgsConstructor
public class AutoCompleteService {
    private final ProjectPathResolver projectPathResolver;

    public Set<String> searchXmlMapperName(String mapperLocationPrefix, String searchKey) {
        Tuple tuple = projectPathResolver.refreshBaseProjectPath(false);

        String mapperRootPath;
        if (CreatorConstant.PACKAGE_RESOURCES_PREFIX.startsWith(mapperLocationPrefix)) {
            mapperRootPath = tuple.get(2);
        } else if (CreatorConstant.PACKAGE_JAVA_PREFIX.startsWith(mapperLocationPrefix)) {
            mapperRootPath = tuple.get(1);
        } else {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.UNRECOGNIZED_SOURCE_CODE_PREFIX,
                mapperLocationPrefix);
        }
        Set<String> hitNames = Sets.newHashSet();
        doSearch(new File(mapperRootPath), CreatorConstant.DOT_XML, searchKey, hitNames, tuple);
        return hitNames;
    }

    private void doSearch(File rootDir, String searchKey, String suffix, Set<String> hitNames, Tuple tuple) {
        if (!FileUtil.exist(rootDir)) {
            return;
        }
        if (!FileUtil.isDirectory(rootDir)) {
            return;
        }
        File[] files = FileUtil.ls(rootDir.getAbsolutePath());
        for (File file : files) {
            if (!file.isDirectory()) {
                String filePackageName =
                    projectPathResolver.convertPathToPackage(file.getAbsolutePath(), tuple.get(1), tuple.get(2));
                if (match(filePackageName, searchKey, suffix)) {
                    hitNames.add(filePackageName.substring(0, filePackageName.length() - suffix.length()));
                }
            } else {
                doSearch(file, suffix, searchKey, hitNames, tuple);
            }
        }
    }

    private boolean match(String name, String searchKey, String suffix) {
        if (!name.endsWith(suffix)) {
            return false;
        }
        if (Strings.isNullOrEmpty(name)) {
            return true;
        }
        return name.toLowerCase().contains(searchKey.toLowerCase());
    }
}
