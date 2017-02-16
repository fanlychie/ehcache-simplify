package org.fanlychie.ehcache.simplify.exception;

/**
 * 找不到缓存异常
 * Created by fanlychie on 2017/2/16.
 */
public class CacheNotFoundException extends RuntimeException {

    public CacheNotFoundException(String message) {
        super(message);
    }

}