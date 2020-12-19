package com.twd.bff.app;

import com.twd.bff.app.biz.feign.SampleFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCircuitBreaker
@EnableAsync
@EnableScheduling
@EnableWebMvc
@EnableFeignClients(clients = SampleFeign.class)
public class TwdBffAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwdBffAppApplication.class, args);
    }

}
