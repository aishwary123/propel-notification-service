package com.notification.propel.configs;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.notification.propel.interfaces.RestAPIPrefix;
import com.notification.propel.messages.CustomMessages;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration for API documentation is done here.
 * 
 * @author aishwaryt
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName(
                    "public-api").apiInfo(apiInfo()).select().paths(
                                postPaths()).build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/api/.*"), regex(RestAPIPrefix.API_BASE_V1 + "/.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(
                    CustomMessages.PROPEL_NOTIFICATION_SERVICER).description(
                                CustomMessages.PROPEL_NOTIFICATION_SERVICER).version(
                                            "1.0").build();
    }

}