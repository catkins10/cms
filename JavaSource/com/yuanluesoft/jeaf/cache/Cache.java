/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.jeaf.cache;

import java.util.Collection;

import com.yuanluesoft.jeaf.cache.exception.CacheException;

/**
 * 
 * @author linchuan
 *
 */
public interface Cache {
	
	/**
	 * 读缓存
	 * @param key
	 * @return the cached object or <tt>null</tt>
	 * @throws CacheException
	 */
	public Object get(Object key) throws CacheException;
	
	/**
	 * 读缓存,但不延长缓存时间
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public Object quietGet(Object key) throws CacheException;
	
	/**
	 * 读缓存,并同步到远程缓存
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public Object synchGet(Object key) throws CacheException;
	
	/**
	 * 写缓存
	 * @param key
	 * @param value
	 * @throws CacheException
	 */
	public void put(Object key, Object value) throws CacheException;
	
	/**
	 * 删除
	 * @param key
	 * @throws CacheException
	 */
	public void remove(Object key) throws CacheException;
	
	/**
	 * 获取关键字列表
	 * @param group
	 * @return
	 * @throws CacheException
	 */
	public Collection getKeys() throws CacheException;
	
	/**
	 * 清除整个缓存
	 * @throws CacheException
	 */
	public void clear() throws CacheException;
}