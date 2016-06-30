package com.yuanluesoft.jeaf.tools.cachetest.actions;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class CacheTestAction extends BaseAction {
	private static int key = 0;
	
	
	/**
	 * 获取cache
	 * @return
	 * @throws SystemUnregistException
	 */
	private Cache getCache() throws SystemUnregistException {
		return (Cache)getBean("modelCache");
	}
	
	/**
	 * 获取cache中的关键字列表
	 * @return
	 */
	protected Set listValues() throws Exception {
		Set values = new HashSet();
		Collection keys = getCache().getKeys();
		if(keys!=null) {
			for(Iterator iterator = keys.iterator(); iterator.hasNext();) {
				Object key = iterator.next();
				CacheTestObject value = (CacheTestObject)getCache().get(key);
				if(value!=null) {
					//value.setValue(System.currentTimeMillis() + "");
					//getCache().put(key, value);
				}
				values.add(value);
			}
		}
		return values;
	}
	
	/**
	 * 填充值到cache
	 * @throws Exception
	 */
	protected void putValue(int count) throws Exception {
		System.out.println("**********put key:" + key + ","  + System.currentTimeMillis());
		InetAddress inet = InetAddress.getLocalHost();
		String ip = inet.getHostAddress();
		Cache cache = getCache();
		for(int i=0; i<count; i++) {
			cache.put(key + "", new CacheTestObject(key + ":" + ip)); //(key-1) + ":" + System.currentTimeMillis() + "");
			key++;
		}
		System.out.println("**********next key:" + key + ","  + System.currentTimeMillis());
	}
	
	/**
	 * 从cache删除值
	 * @throws Exception
	 */
	protected void removeValue() throws Exception {
		getCache().remove("" + (--key));
	}
	
	/**
	 * 从cache删除全部值
	 * @throws Exception
	 */
	protected void removeAllValue() throws Exception {
		getCache().clear();
		key = 0;
	}
}
