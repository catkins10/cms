/**
 * 
 */
package com.yuanluesoft.fet.tradestat.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * @author yuanluesoft
 *
 */
public interface FetCompanyService extends BusinessService {
	public static final int USER_TYPE_COMPANY = PersonService.PERSON_TYPE_OTHER + 1;
	public static final int USER_TYPE_DEVELOPMENT_AREA = PersonService.PERSON_TYPE_OTHER + 2;
	public static final int USER_TYPE_COUNTY = PersonService.PERSON_TYPE_OTHER + 3;
	
	/**
	 * 获取用户有访问权限的区县列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listPermitCounties(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户有访问权限的开发区列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listPermitDevelopmentAreas(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 加密口令
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public String cryptPassword(String password) throws ServiceException;
}
