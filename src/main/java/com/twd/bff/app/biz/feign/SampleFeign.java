package com.twd.bff.app.biz.feign;

import com.twd.bff.app.config.FeignRetryConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sample", url = "${external-api.http-bin}", configuration = {FeignRetryConfiguration.class})
public interface SampleFeign {
    @GetMapping("/api/v1/sample/feignHello")
    @Headers("X-Auth-Token: {name}")
    String hello(@RequestParam String name); // 주의! @RequestParam을 제외 하면 POST로 강제로 바뀜
}
