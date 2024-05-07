package com.vk.common.swagger.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@Import({ SwaggerWebConfiguration.class})
public class SwaggerAutoConfiguration
{
    /**
     * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     */
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    private static final String BASE_PATH = "/**";


    @Bean
    public OpenAPI swaggerOpenApi(SwaggerProperties sw) {
        OpenAPI api = new OpenAPI();
        return api
                .info(apiInfo(sw))
                .externalDocs(new ExternalDocumentation()
                        .description("设计文档")
                        .url("https://juejin.cn/user/254742430749736/posts"));
    }

    private Info apiInfo(SwaggerProperties sw)
    {
        return new Info()
                .title(sw.getTitle())
                .description(sw.getDescription())
                .license(new License().name(sw.getLicense()).url(sw.getLicenseUrl()))
                .contact(new Contact().name(sw.getContact().getName()).url(sw.getContact().getUrl()).email(sw.getContact().getEmail()))
                .version(sw.getVersion());
    }

}
