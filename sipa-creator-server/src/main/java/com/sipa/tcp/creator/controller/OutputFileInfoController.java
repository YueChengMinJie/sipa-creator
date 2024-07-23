package com.sipa.tcp.creator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.sipa.tcp.creator.mbp.OutputFileInfo;
import com.sipa.tcp.creator.mbp.ProjectPathResolver;
import com.sipa.tcp.creator.mbp.UserConfig;
import com.sipa.tcp.creator.mbp.UserConfigStore;
import com.sipa.tcp.creator.service.OutputFileInfoService;
import com.sipa.tcp.creator.strategy.*;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/output-file-info")
public class OutputFileInfoController {
    private final OutputFileInfoService outputFileInfoService;

    private final UserConfigStore userConfigStore;

    private final ProjectPathResolver projectPathResolver;

    @GetMapping("/user-config")
    public UserConfig getUserConfig() {
        return userConfigStore.getDefaultUserConfig();
    }

    @GetMapping("/project-root-path")
    public String getRootPath() {
        return projectPathResolver.getBaseProjectPath();
    }

    @PostMapping("/delete")
    public void deleteOutputInfos(@RequestBody OutputFileInfo outputFileInfo) {
        outputFileInfoService.deleteOutputFileInfo(outputFileInfo);
    }

    @PostMapping("/save")
    public void saveOutputInfos(@RequestBody OutputFileInfo outputFileInfo) {
        outputFileInfoService.saveOutputFileInfo(outputFileInfo);
    }

    @PostMapping("/save-entity-strategy")
    public void saveEntityStrategy(@RequestBody EntityStrategy entityStrategy) {
        outputFileInfoService.saveEntityStrategy(entityStrategy);
    }

    @PostMapping("/save-mapper-strategy")
    public void saveMapperStrategy(@RequestBody MapperStrategy mapperStrategy) {
        outputFileInfoService.saveMapperStrategy(mapperStrategy);
    }

    @PostMapping("/save-mapper-xml-strategy")
    public void saveMapperXmlStrategy(@RequestBody MapperXmlStrategy mapperXmlStrategy) {
        outputFileInfoService.saveMapperXmlStrategy(mapperXmlStrategy);
    }

    @PostMapping("/save-controller-strategy")
    public void saveControllerStrategy(@RequestBody ControllerStrategy controllerStrategy) {
        outputFileInfoService.saveControllerStrategy(controllerStrategy);
    }

    @PostMapping("/save-service-strategy")
    public void saveServiceStrategy(@RequestBody ServiceStrategy serviceStrategy) {
        outputFileInfoService.saveServiceStrategy(serviceStrategy);
    }

    @PostMapping("/save-service-impl-strategy")
    public void saveServiceImplStrategy(@RequestBody ServiceImplStrategy serviceImplStrategy) {
        outputFileInfoService.saveServiceImplStrategy(serviceImplStrategy);
    }

    /**
     * 查看当前项目是否存在配置文件
     */
    @GetMapping("/check-if-new-project")
    public boolean checkIfNewProject() {
        return userConfigStore.checkUserConfigExisted();
    }

    /**
     * 获取本机所有已保存配置的项目列表
     */
    @GetMapping("/all-saved-project")
    public List<String> getAllSavedProject() {
        return userConfigStore.getAllSavedProject();
    }

    /**
     * 为当前项目导入其它项目的配置文件
     */
    @PostMapping("/import-project-config/{sourceProjectPkg}")
    public void importProjectConfig(@PathVariable("sourceProjectPkg") String sourceProjectPkg) {
        userConfigStore.importProjectConfig(sourceProjectPkg);
    }
}
