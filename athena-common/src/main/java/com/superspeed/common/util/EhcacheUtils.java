package com.superspeed.common.util;

import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * Ehcache工具类
 * @ClassName: EhcacheUtils
 * @Description: 提供一些常用的操作缓存方法
 * @author xc.yanww
 * @date 2017-07-17 下午4:09:39
 * @version  1.0
 */
public class EhcacheUtils {
	/**
	 * 系统全局变量 "SYS_CACHE_" + 全局变量名称(大写)
	 */
	private  static  final  String SYS_CACHE = "sysCache";
	/**
	 * 字典全局变量  "DICT_INFO_" + itemCode + dictValue | "DICT_" + itemCode
	 */
	private  static  final  String DICT_CACHE = "dictCache";
	/**
	 * 用户缓存(包含用户信息缓存，用户菜单缓存，角色数据权限缓存)
	 * 用户信息缓存 "USER_CACHE_" + userId/loginName
	 * 用户菜单缓存 "USER_RESOURCE_" + userId/loginName
	 * 角色数据权限缓存 "ROLE_RULE_" + userId/loginName
	 */
	private  static  final  String USER_CACHE = "userCache";
	
	/**
	 * 获取EhCacheManager实例
	 * @author xc.yanww
	 * @date 2017-07-17 下午4:26:36
	 * @return EhCacheManager对象实例
	 */
	public static EhCacheCacheManager getCacheManager() {
		return (EhCacheCacheManager) SpringContextUtils.getBean("cacheManager");
	}
	
	/**
	 * 把对象存放到缓存中
	 * @author xc.yanww
	 * @date 2017-07-17 下午4:16:43
	 * @param cacheName 缓存名称
	 * @param key 键
	 * @param value 对象值
	 */
	public static void put(String cacheName, Object key, Object value) {
		EhCacheCacheManager cacheManager = getCacheManager();
		cacheManager.getCache(cacheName).put(key, value);
	}
	
	/**
	 * 从缓存中获取对象
	 * @author xc.yanww
	 * @date 2017-07-17 下午4:18:09
	 * @param cacheName 缓存名称
	 * @param key 键
	 * @return 对象值
	 */
	public static Object get(String cacheName, Object key) {
		EhCacheCacheManager cacheManager = getCacheManager();
		return cacheManager.getCache(cacheName).get(key);
	}
	
	/**
	 * 把对象从缓存中移除
	 * @author xc.yanww
	 * @date 2017-07-17 下午4:16:43
	 * @param cacheName 缓存名称
	 * @param key 键
	 */
	public static void remove(String cacheName, Object key) {
		EhCacheCacheManager cacheManager = getCacheManager();
		cacheManager.getCache(cacheName).evict(key);
	}

	/**
	 * 移除缓存中所有对象
	 * @author xc.yanww
	 * @date 2017-07-17 下午4:16:43
	 * @param cacheName 缓存名称
	 */
	public static void removeAll(String cacheName) {
		EhCacheCacheManager cacheManager = getCacheManager();
		cacheManager.getCache(cacheName).clear();
	}

}
