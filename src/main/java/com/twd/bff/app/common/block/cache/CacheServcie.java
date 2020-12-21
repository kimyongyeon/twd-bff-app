package com.twd.bff.app.common.block.cache;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis Cache 서비스 인터페이스
 */
public interface

CacheServcie {

    /**
     * Get key's entry name
     *
     * @param entry
     * @return Name of the entry
     */
    String getEntryName(String entry);

    /**
     * Get Redis Template
     *
     * @return Object of Redis Template
     */
    RedisTemplate<String, Object> getRedisTemplate();

    /**
     * Get a key by ID and entry name
     *
     * @param metaSet
     * @param id
     * @param clazz
     * @return Class<T> type key/value
     */
    <T> T getKeyByEntryNameAndId(String metaSet, String id, Class<T> clazz);

    /**
     * Get keys by entry name
     *
     * @param metaSet
     * @return object type keys
     */
    Object getKeysByEntryName(CacheConstants.REDIS_CACHE metaSet);


    /**
     * Get keys by entry name
     *
     * @param metaSet
     * @param clazz
     * @return Class<T> type keys
     */
    <T> T getKeysByEntryName(CacheConstants.REDIS_CACHE metaSet, Class<T> clazz);


    /**
     * Get a key by ID and entry name
     *
     * @param metaSet
     * @param id
     * @return Object type key/value
     */
    Object getKeyByEntryNameAndId(CacheConstants.REDIS_CACHE metaSet, String id);

    /**
     * Get a key by ID and entry name
     *
     * @param metaSet
     * @param id
     * @param clazz
     * @return Class<T> type key/value
     */
    <T> T getKeyByEntryNameAndId(CacheConstants.REDIS_CACHE metaSet, String id, Class<T> clazz);
}
