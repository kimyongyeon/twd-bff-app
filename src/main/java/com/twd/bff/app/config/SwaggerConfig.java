package com.twd.bff.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    private String version;
    private String title;

    @Bean
    public Docket api() {

        version = "V1";
        title = "Tworld Direct BFF API " + version;

        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(200)
                .message("OK ~~")
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(404)
                .message("Not Found ~~")
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(500)
                .message("Internal Server Error ~~")
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.twd.bff.app.biz"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .apiInfo(apiInfo(title, version))
                .globalResponseMessage(RequestMethod.GET, responseMessages);
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Tworld Direct BFF API 문서 입니다.",
                version,
                "www.twd.com",
                new Contact("Contact Me", "www.twd.com", "admin@twd.com"),
                "Licenses",
                "www.twd.com",
                new ArrayList<>());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

