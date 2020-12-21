package com.twd.bff.app.common.block.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;


public abstract class BaseCacheService implements CacheServcie {

    @Override
    public String getEntryName(final String entry) {
        return entry;
    }

    @Override
    public <T> T getKeyByEntryNameAndId(final String metaSet, final String id, final Class<T> clazz) {
        final Object result = getRedisTemplate().opsForHash().entries(getEntryName(metaSet)).get(id);
        return RedisLoaderUtils.convertInstanceOfObject(result, clazz);
    }

    @Override
    public Object getKeysByEntryName(final CacheConstants.REDIS_CACHE metaSet) {
        return getObjectKeysByEntryName(metaSet);
    }

    @Override
    public <T> T getKeysByEntryName(final CacheConstants.REDIS_CACHE metaSet, final Class<T> clazz) {
        Object result = getObjectKeysByEntryName(metaSet);
        return result == null ? null : RedisLoaderUtils.convertInstanceOfObject(result, clazz);
    }

    private Object getObjectKeysByEntryName(final CacheConstants.REDIS_CACHE metaSet) {
        Object result =null;

        if(metaSet.isValue()) {
            if(metaSet.isGroup()) {
                Cursor<byte[]> cursor = getRedisTemplate()
                        .getConnectionFactory()
                        .getConnection()
                        .scan(ScanOptions.scanOptions()
                                .match(getEntryName(metaSet.name()) + ":*")
                                .count(10000).build());

                List<String> keys = new ArrayList<>();
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));// 조회된 Key의 설정
                }

                Map<String, Object> map = new HashMap<>();
                keys.forEach(key -> {
                    try {
                        map.put(key, getRedisTemplate().opsForValue().get(key));
                    } catch (Exception e) {
                        throw e;
                    }
                });

                result = map;
            }else {
                result = getRedisTemplate().opsForValue().get(metaSet.name());
            }
        } else if(metaSet.isHash()) {
            if(metaSet.isGroup()) {
                result = getRedisTemplate().opsForHash().entries(getEntryName(metaSet.name()) + ":*");
            } else {
                result = getRedisTemplate().opsForHash().entries(metaSet.name());
            }
        }

        return result;
    }

    @Override
    public Object getKeyByEntryNameAndId(final CacheConstants.REDIS_CACHE metaSet, final String id) {
        return getObjectKeyByEntryNameAndId(metaSet, id);
    }

    @Override
    public <T> T getKeyByEntryNameAndId(final CacheConstants.REDIS_CACHE metaSet, final String id, final Class<T> clazz) {
        Object result = getObjectKeyByEntryNameAndId(metaSet, id);
        return result == null ? null : RedisLoaderUtils.convertInstanceOfObject(result, clazz);
    }

    private Object getObjectKeyByEntryNameAndId(final CacheConstants.REDIS_CACHE metaSet, final String id) {
        Object result = null;

        if(metaSet.isValue()) {
            if(metaSet.isGroup()) {
                result = getRedisTemplate().opsForValue().get(getEntryName(metaSet.name() + ":" + id));
            }else {
                result = getRedisTemplate().opsForValue().get(id);
            }
        } else if(metaSet.isHash()) {
            if(metaSet.isGroup()) {
                result = getRedisTemplate().opsForHash().entries(getEntryName(metaSet.name()) + ":" + id);
            } else {
                result = getRedisTemplate().opsForHash().get(getEntryName(metaSet.name()), id);
            }
        }

        return result;
    }
}

