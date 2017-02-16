package org.fanlychie.ehcache.simplify;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.fanlychie.ehcache.simplify.exception.CacheNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * ehcache 缓存管理
 * Created by fanlychie on 2017/2/16.
 */
public final class EhcacheManager {

    /**
     * 配置文件
     */
    private static String configFile;

    /**
     * 缓存管理
     */
    private static CacheManager cacheManager;

    /**
     * 缓存日志
     */
    private static Logger logger = LoggerFactory.getLogger(EhcacheManager.class);

    /**
     * 获取缓存管理对象
     *
     * @return 返回缓存管理对象
     */
    public static CacheManager getCacheManager() {
        if (cacheManager == null) {
            if (configFile == null) {
                log("使用 ehcache.xml 配置创建缓存管理器");
                cacheManager = CacheManager.newInstance();
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("「EHCACHE」使用 " + configFile + " 配置创建缓存管理器");
                }
                log("使用 " + configFile + " 配置创建缓存管理器");
                cacheManager = CacheManager.newInstance(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile));
            }
        }
        return cacheManager;
    }

    /**
     * 获取缓存对象
     *
     * @param name 缓存名称
     * @return 返回缓存对象
     */
    public static Cache getCache(String name) {
        Cache cache = getCacheManager().getCache(name);
        if (cache == null) {
            throw new CacheNotFoundException("找不到名称为 \"" + name + "\" 的缓存");
        }
        return cache;
    }

    /**
     * 将键值对放入缓存对象
     *
     * @param cacheName 缓存名称
     * @param key       键名称
     * @param value     键的值
     */
    public static void put(String cacheName, Serializable key, Serializable value) {
        put(getCache(cacheName), key, value);
    }

    /**
     * 将键值对放入缓存对象
     *
     * @param cache 缓存对象
     * @param key   键名称
     * @param value 键的值
     */
    public static void put(Cache cache, Serializable key, Serializable value) {
        cache.put(new Element(key, value));
        log("PUT  " + key);
        log("==>  " + value);
    }

    /**
     * 获取缓存中的值
     *
     * @param cacheName 缓存名称
     * @param key       键名称
     * @param <T>       期望的类型
     * @return 返回缓存中键关联的值
     */
    public static <T> T get(String cacheName, Serializable key) {
        return get(getCache(cacheName), key);
    }

    /**
     * 获取缓存中的值
     *
     * @param cache 缓存对象
     * @param key   键名称
     * @param <T>   期望的类型
     * @return 返回缓存中键关联的值
     */
    public static <T> T get(Cache cache, Serializable key) {
        T value = null;
        Element element = cache.get(key);
        if (element != null) {
            value = (T) element.getObjectValue();
        }
        log("GET  " + key);
        log("<==  " + value);
        return value;
    }

    /**
     * 获取缓存中的键名称集合
     *
     * @param cacheName 缓存名称
     * @return 返回缓存中的键名称集合
     */
    public static List getKeys(String cacheName) {
        return getKeys(getCache(cacheName));
    }

    /**
     * 获取缓存中的键名称集合
     *
     * @param cache 缓存对象
     * @return 返回缓存中的键名称集合
     */
    public static List getKeys(Cache cache) {
        return cache.getKeys();
    }

    /**
     * 从缓存中移除键值对
     *
     * @param cacheName 缓存名称
     * @param key       键名称
     */
    public static void remove(String cacheName, Serializable key) {
        remove(getCache(cacheName), key);
    }

    /**
     * 从缓存中移除键值对
     *
     * @param cache 缓存对象
     * @param key   键名称
     */
    public static void remove(Cache cache, Serializable key) {
        cache.remove(key);
        log("DEL  " + key);
    }

    /**
     * 从缓存中移除键值对集合
     *
     * @param cacheName 缓存名称
     * @param keys      键名称集合
     */
    public static void remove(String cacheName, Collection keys) {
        remove(getCache(cacheName), keys);
    }

    /**
     * 从缓存中移除键值对集合
     *
     * @param cache 缓存对象
     * @param keys  键名称集合
     */
    public static void remove(Cache cache, Collection keys) {
        cache.removeAll(keys);
        log("DEL  " + Arrays.toString(keys.toArray()));
    }

    /**
     * 清空缓存
     *
     * @param cacheName 缓存名称
     */
    public static void clear(String cacheName) {
        clear(getCache(cacheName));
    }

    /**
     * 清空缓存
     *
     * @param cache 缓存对象
     */
    public static void clear(Cache cache) {
        cache.removeAll();
        log("CLEAR  " + cache.getName());
    }

    /**
     * 设置配置文件, 路径相对类路径, 默认加载类路径下的 ehcache.xml 配置文件
     *
     * @param configFile 配置文件
     */
    public static void setConfigFile(String configFile) {
        EhcacheManager.configFile = configFile;
    }

    // 私有化
    private EhcacheManager() {

    }

    // 记录日志
    private static void log(String message) {
        if (logger.isInfoEnabled()) {
            logger.info("「EHCACHE」" + message);
        }
    }

}