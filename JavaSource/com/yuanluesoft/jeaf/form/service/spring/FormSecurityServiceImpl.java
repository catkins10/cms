package com.yuanluesoft.jeaf.form.service.spring;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class FormSecurityServiceImpl implements FormSecurityService {
	private Cache requestCodeCache; //请求编码缓存

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormSecurityService#registRequest(boolean)
	 */
	public String registRequest(boolean validateCodeImageRequired) throws ServiceException {
		String requestCode = UUIDStringGenerator.generateId();
		try {
			requestCodeCache.put(requestCode, new Boolean(validateCodeImageRequired));
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
		return requestCode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormSecurityService#setValidateCodeImageRequired(java.lang.String, boolean)
	 */
	public void setValidateCodeImageRequired(String requestCode, boolean validateCodeImageRequired) throws ServiceException {
		try {
			requestCodeCache.put(requestCode, new Boolean(validateCodeImageRequired));
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormSecurityService#removeRequest(java.lang.String)
	 */
	public void removeRequest(String requestCode) throws ServiceException {
		if(requestCode==null || requestCode.isEmpty()) {
			return;
		}
		try {
			requestCodeCache.remove(requestCode);
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormSecurityService#validateRequestCode(java.lang.String)
	 */
	public boolean validateRequest(String requestCode) throws ServiceException {
		try {
			return requestCodeCache.get(requestCode)!=null;
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormSecurityService#isValidateCodeImageRequired(java.lang.String)
	 */
	public boolean isValidateCodeImageRequired(String requestCode) throws ServiceException {
		try {
			Boolean required = (Boolean)requestCodeCache.get(requestCode);
			return required!=null && required.booleanValue();
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * @return the requestCodeCache
	 */
	public Cache getRequestCodeCache() {
		return requestCodeCache;
	}

	/**
	 * @param requestCodeCache the requestCodeCache to set
	 */
	public void setRequestCodeCache(Cache requestCodeCache) {
		this.requestCodeCache = requestCodeCache;
	}
}