package com.omron.omd.util;

import com.jfinal.plugin.ehcache.CacheKit;

/**
 * EhCache 工具类
 *
 * @author zxy
 */
public final class EhCacheUtil {

    private static final String CACHE_NAME = "app";

    /**
     * 放入缓存
     *
     * @param id    数据唯一标识
     * @param key   键
     * @param value 值
     */
    public static void put(String id, String key, Object value) {
        CacheKit.put(CACHE_NAME, id + "." + key, value);
    }

    /**
     * 获取缓存
     *
     * @param id  数据唯一标识
     * @param key 键
     * @param <T> 值
     * @return <T> T
     */
    public static <T> T get(String id, String key) {
        return CacheKit.get(CACHE_NAME, id + "." + key);
    }

    /**
     * 获取缓存
     *
     * @param id     数据唯一标识
     * @param key    键
     * @param defVal 默认值
     * @param <T>    值
     * @return <T> T
     */
    public static <T> T get(String id, String key, T defVal) {
        T val = CacheKit.get(CACHE_NAME, id + "." + key);
        return val == null ? defVal : val;
    }

    /**
     * 获取缓存
     *
     * @param id  数据唯一标识
     * @param key 键
     * @return boolean
     */
    public static boolean has(String id, String key) {
        return null != get(id, key);
    }
}
