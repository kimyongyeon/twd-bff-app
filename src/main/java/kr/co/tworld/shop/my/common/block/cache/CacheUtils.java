package kr.co.tworld.shop.my.common.block.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * CacheUtils
 */
@Component
public class CacheUtils {

    @Autowired
    CommonCacheService commonCacheService;

    private static CommonCacheService cacheService;

    @PostConstruct
    private void init() {
        CacheUtils.cacheService = commonCacheService;
    }

    private static CommonCacheService getCacheService() {
        return CacheUtils.cacheService;
    }

    /**
     * 메타데이터 조회
     *
     * @param metaSet : 메타데이터 명
     * @param id      : Key
     * @param clazz   : Return Model
     * @return <class> 메타 데이터 (1건)
     */
    public static <T> T getCacheData(final String metaSet, final String id, final Class<T> clazz) {
        return getCacheService().getKeyByEntryNameAndId(metaSet, id, clazz);
    }

    /**
     * 메타데이터 조회
     *
     * @param metaSet : 메타데이터 명
     * @return Object(<Map>) 메타 데이터
     */
    public static Object getCacheData(final CacheConstants.REDIS_CACHE metaSet) {
        return getCacheService().getKeysByEntryName(metaSet);
    }


    /**
     * 메타데이터 조회
     *
     * @param metaSet : 메타데이터 명 (enum REDIS_CACHE)
     * @param id      : Key
     * @return Object(<Map>) 메타 데이터
     */
    public static Object getCacheData(final CacheConstants.REDIS_CACHE metaSet, final String id) {
        return getCacheService().getKeyByEntryNameAndId(metaSet, id);
    }

    /**
     * 메타데이터 조회
     *
     * @param metaSet : 메타데이터 명 (enum REDIS_CACHE)
     * @param id      : Key
     * @param clazz   : Return Model
     * @return <class> 메타 데이터 (1건)
     */
    public static <T> T getCacheData(final CacheConstants.REDIS_CACHE metaSet, final String id, final Class<T> clazz) {
        return getCacheService().getKeyByEntryNameAndId(metaSet, id, clazz);
    }
}

