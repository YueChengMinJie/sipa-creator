package com.sipa.tcp.creator.configuration;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.sipa.tcp.creator.mbp.BeetlTemplateEngine;
import com.sipa.tcp.creator.mbp.DynamicParamSqlEnhancer;
import com.sipa.tcp.creator.mbp.UserConfigStore;
import com.sipa.tcp.creator.property.GeneratorConfigProperty;

import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Configuration
@AllArgsConstructor
@ComponentScan("com.sipa.tcp.creator.**")
@EnableConfigurationProperties(GeneratorConfigProperty.class)
public class CreatorConfiguration implements WebMvcConfigurer {
    private final GeneratorConfigProperty property;

    private final UserConfigStore userConfigStore;

    private final DynamicDataSourceProperties properties;

    @Bean
    public Map<String, DataSourceConfig> mbpDsConfigs() {
        return properties.getDatasource().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            DataSourceProperty p = entry.getValue();
            return new DataSourceConfig.Builder(p.getUrl(), p.getUsername(), p.getPassword())
                .schema(property.getSchemaName())
                .typeConvert(property.getTypeConvert())
                .build();
        }));
    }

    @Bean
    public DynamicParamSqlEnhancer dynamicParamSqlEnhancer() {
        return new DynamicParamSqlEnhancer(property.getDbType());
    }

    @Bean
    public BeetlTemplateEngine beetlTemplateEngine() {
        return new BeetlTemplateEngine(property.getNameConverter(), userConfigStore.getTemplateStoreDir());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/_nuxt/**").addResourceLocations("classpath:/public/_nuxt/");
        registry.addResourceHandler("/libs/**").addResourceLocations("classpath:/public/libs/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/public/");
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new MyErrorPageRegistrar();
    }

    private static class MyErrorPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
        }
    }
}
