package com.twd.bff.app.common.block.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * RedisLoaderUtils
 */
@Slf4j
public class RedisLoaderUtils {

    private static ObjectMapper objectMapper;

    public static void setObjectMapper() {
        RedisLoaderUtils.setObjectMapper(null);
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        if (RedisLoaderUtils.objectMapper == null) {
            if (objectMapper == null) {
                RedisLoaderUtils.objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
                log.debug("Jackson2ObjectMapperBuilder.json().createXmlMapper(false) : {}", RedisLoaderUtils.objectMapper);
            } else {
                RedisLoaderUtils.objectMapper = objectMapper;
                log.debug("objectMapper : {}", RedisLoaderUtils.objectMapper);
            }
        }
    }

    /**
     * @param value
     * @param redisSerializer
     * @return byte[] serialized data for I/O
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static byte[] serialize(final Object value, final RedisSerializer redisSerializer) {
        if (redisSerializer == null && value instanceof byte[]) {
            return (byte[]) value;
        }
        return redisSerializer.serialize(value);
    }

    /**
     * Convert an instance
     *
     * @param o, object that for convert
     * @param clazz, class that to convert
     * @return converted object
     */
    public static <T> T convertInstanceOfObject(final Object o, final Class<T> clazz) {
        if(o == null) {
            return null;
        }

        try {
            return RedisLoaderUtils.objectMapper.convertValue(o, clazz);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param type
     * @param template
     * @return
     */
    public static <K, V> RedisTemplate<?, ?> redisTemplateKeySerializer(final CacheConstants.RedisSerializerTypes type,
                                                                        final RedisTemplate<K, V> template) {
        if (CacheConstants.RedisSerializerTypes.String.equals(type)) {
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
        } else if (CacheConstants.RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        } else {
            template.setKeySerializer(new JdkSerializationRedisSerializer());
            template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }

    /**
     * @param type
     * @param template
     * @return
     */
    public static <K, V> RedisTemplate<?, ?> redisTemplateValueSerializer(final CacheConstants.RedisSerializerTypes type,
                                                                          final RedisTemplate<K, V> template) {
        if (CacheConstants.RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer(RedisLoaderUtils.objectMapper));
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(RedisLoaderUtils.objectMapper));
        } else if (CacheConstants.RedisSerializerTypes.String.equals(type)) {
            template.setValueSerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new StringRedisSerializer());
        } else {
            template.setValueSerializer(new JdkSerializationRedisSerializer());
            template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }
}

