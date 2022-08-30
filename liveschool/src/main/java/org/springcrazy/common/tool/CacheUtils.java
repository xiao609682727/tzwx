package org.springcrazy.common.tool;

import org.springcrazy.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheUtils {
    private static CacheManager manager;

    @Autowired
    public void setManager(CacheManager manager) {
        CacheUtils.manager = manager;
    }

    public static Object get(Object key) {
        Cache.ValueWrapper cache = manager.getCache("cache").get(key);
        if(Func.isNotEmpty(cache)){
            return cache.get();
        }
        return null;
    }

    public static void put( Object key, Object value) {
        manager.getCache("cache").put(key,value);
    }

    public static boolean remove(Object key) {
        return manager.getCache("cache").evictIfPresent(key);
    }

    public static void removeAll(String cacheName) {
        manager.getCache("cache").clear();
    }


}
