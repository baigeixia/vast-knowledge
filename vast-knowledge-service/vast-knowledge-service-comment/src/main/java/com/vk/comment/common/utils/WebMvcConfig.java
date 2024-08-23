package com.vk.comment.common.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 设置首页
     *
     * @param registry
     * @return void
     * @author wliduo[i@dolyw.com]
     * @date 2019/1/24 19:18
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/view.shtml");
        registry.setOrder(Ordered.LOWEST_PRECEDENCE);
    }

}
