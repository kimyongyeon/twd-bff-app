package com.twd.bff.app.common.block.cache;

/**
 * CacheConstants
 */
public class CacheConstants {

    public static final String PREFIX_BASE_SPRING_PROPERTIES = "spring";
    public static final String PREFIX_BASE_TDIRECT_PROPERTIES = "tdirect";
    public static final String PREFIX_BASE_REDIS_PROPERTIES = "redis";

    public static final String PREFIX_DEFAULT_REDIS_PROPERTIES = PREFIX_BASE_SPRING_PROPERTIES + "." + PREFIX_BASE_REDIS_PROPERTIES;
    public static final String PREFIX_BASE_TWORLD_REDIS_PROPERTIES = PREFIX_BASE_TDIRECT_PROPERTIES + "." + PREFIX_BASE_REDIS_PROPERTIES;

    /* List of a Cache Service */
    public static enum REDIS_CACHE {
        Code("Value", true),
        UrlBlockMeta("Hash", false),
        TssMeta("Hash", false);

        private String type;
        private boolean group;

        REDIS_CACHE (String type, boolean group) {
            this.type = type;
            this.group = group;
        }

        public String getType() {
            return this.type;
        }

        public boolean isValue() {
            return this.type == "Value";
        }

        public boolean isHash() {
            return this.type == "Hash";
        }

        public boolean isGroup() {
            return this.group;
        }
    };

    public static enum RedisSerializerTypes {
        String, GenericJackson2Json, JdkSerialization
    };
}

