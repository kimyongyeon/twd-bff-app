package kr.co.tworld.shop.my.config;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

import static java.lang.String.format;

public class FeignRetryConfiguration {
    @Bean
    public Retryer retryer() {
        // 재시도 1초부터 최대 2초 재시도 최대 3번 재시도
        return new Retryer.Default(1000, 2000, 3);
    }

    @Bean
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
            if (response.status() == 500) {
                return new RetryableException(format("%s 요청이 성공하지 못했습니다. Retry 합니다. - status: %s, headers: %s", methodKey, response.status(), response.headers()), null);
            }

            return new IllegalStateException(format("%s 요청이 성공하지 못했습니다. - status: %s, headers: %s",
                    methodKey, response.status(), response.headers()));
        };
    }
}
