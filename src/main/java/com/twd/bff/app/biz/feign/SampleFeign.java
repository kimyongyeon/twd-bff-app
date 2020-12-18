package com.twd.bff.app.biz.feign;

import com.twd.bff.app.config.FeignRetryConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "sample-feign", url = "${external-api.http-bin}", configuration = {FeignRetryConfiguration.class})
public interface SampleFeign {
    @GetMapping("/status/{status}")
    @feign.Headers("key3: value3")
    void status(@PathVariable("status") int status);
}
