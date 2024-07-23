package com.sipa.tcp.creator.mbp;

import java.util.List;

import com.sipa.tcp.creator.util.PathUtil;

import lombok.Data;

/**
 * 定义生成代码时每次可能发生变化的一些配置项目
 * 
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class GenSetting {
    /**
     * 需要生成的文件类型
     */
    private List<String> choosedOutputFiles;

    /**
     * 文件存在时是否覆盖
     */
    private boolean override;

    /**
     * 注释的作者
     */
    private String author;

    /**
     * 功能模块名
     */
    private String moduleName;

    /**
     * 临时模板参数, 由用户自行输入的临时参数, 用于控制生成文件的可选部分
     */
    private List<String> choosedControllerMethods;

    /**
     * 目标项目根目录
     */
    private String rootPath = PathUtil.getDefaultPath();
}
