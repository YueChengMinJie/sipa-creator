package com.sipa.tcp.creator.strategy;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class ControllerStrategy {
    /**
     * 自定义继承的Controller类全称, 带包名
     */
    private String superControllerClass;

    /**
     * 生成 <code>@RestController</code> 控制器
     * 
     * <pre>
     *      <code>@Controller</code> -> <code>@RestController</code>
     * </pre>
     */
    private boolean restControllerStyle = true;

    /**
     * 驼峰转连字符
     * 
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private boolean controllerMappingHyphenStyle = true;
}
