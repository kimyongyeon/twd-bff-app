package com.twd.bff.app.common.block.cache;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommonCacheService extends BaseCacheService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> template;

    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.template;
    }
}
