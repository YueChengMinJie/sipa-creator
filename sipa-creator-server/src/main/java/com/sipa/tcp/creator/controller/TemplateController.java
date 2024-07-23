package com.sipa.tcp.creator.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sipa.boot.core.exception.system.ESystemErrorCode;
import com.sipa.boot.core.exception.system.SystemExceptionFactory;
import com.sipa.boot.core.response.ResponseWrapper;
import com.sipa.tcp.creator.mbp.OutputFileInfo;
import com.sipa.tcp.creator.mbp.UserConfig;
import com.sipa.tcp.creator.mbp.UserConfigStore;
import com.sipa.tcp.creator.util.TemplateUtil;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/template")
public class TemplateController {
    private final UserConfigStore userConfigStore;

    @GetMapping("/download")
    @SneakyThrows(IOException.class)
    public ResponseWrapper<byte[]> download(@RequestParam String fileType) {
        if (Strings.isNullOrEmpty(fileType)) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.FILE_TYPE_CANNOT_BE_EMPTY);
        }
        UserConfig userConfig = userConfigStore.getUserConfigFromFile();
        if (userConfig == null) {
            InputStream tplIn = TemplateUtil.getBuiltInTemplate(fileType);
            return toDownloadEntity(tplIn);
        }
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        for (OutputFileInfo fileInfo : fileInfos) {
            if (fileType.equals(fileInfo.getFileType())) {
                if (fileInfo.isBuiltIn() && Strings.isNullOrEmpty(fileInfo.getTemplatePath())) {
                    InputStream tplIn = TemplateUtil.getBuiltInTemplate(fileType);
                    return toDownloadEntity(tplIn);
                } else {
                    String tplPath = fileInfo.getTemplatePath();
                    if (tplPath.startsWith("file:")) {
                        tplPath = tplPath.replaceFirst("file:", "");
                    }
                    File tplFile = new File(tplPath);
                    if (tplFile.exists()) {
                        return toDownloadEntity(Files.newInputStream(tplFile.toPath()));
                    } else {
                        throw SystemExceptionFactory.bizException(ESystemErrorCode.TEMPLATE_FILE_NOT_FOUND,
                            fileInfo.getTemplatePath());
                    }
                }
            }
        }
        throw SystemExceptionFactory.bizException(ESystemErrorCode.FILE_NOT_FOUND);
    }

    /**
     * 从输入流构建http响应
     *
     * @param tplIn
     *            流
     * @return http响应
     */
    private ResponseWrapper<byte[]> toDownloadEntity(InputStream tplIn) {
        if (tplIn == null) {
            throw SystemExceptionFactory.bizException(ESystemErrorCode.FILE_NOT_FOUND);
        }
        return ResponseWrapper.success(IoUtil.readBytes(tplIn));
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> params = Maps.newHashMap();
        String storePath = userConfigStore.uploadTemplate(file);
        params.put("templatePath", storePath);
        params.put("templateName", file.getOriginalFilename());
        return params;
    }
}
