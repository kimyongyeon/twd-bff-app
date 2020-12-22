package kr.co.tworld.shop.my.config;

import kr.co.tworld.shop.my.common.logging.LoggingFilter;
import kr.co.tworld.shop.my.common.logging.RequestProcessingTimeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value(value = "${maxAgeSeconds:1000}")
    private static final long MAX_AGE_SECONDS = 3600;

    @Value("${spring.profiles.active}")
    String springProfilesActive;

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean()
    {
        log.debug("springProfilesActive::::" + springProfilesActive);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new LoggingFilter(springProfilesActive));
        // registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new BlockUrlInterceptor());
        registry.addInterceptor(new RequestProcessingTimeInterceptor())
                .addPathPatterns("/api/v1/**")        // 인터셉터 적용할 url
        ;
    }

    /**
     * core에서 설정함.
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(MAX_AGE_SECONDS);
            }
        };
    }

    @Bean(name = "asyncTask")
    public TaskExecutor asyncTask() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("asyncTask-");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(0);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }


    /**
     * false : 점이 포함 된 요청 URI의 끝을 매핑 할 때 마지막 점을 잘라서 처리 않도록함.
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    /**
     * false : 파일 확장자를 사용하여 요청의 accept mediaType을 오버라이드하지 않도록 처리.
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}

