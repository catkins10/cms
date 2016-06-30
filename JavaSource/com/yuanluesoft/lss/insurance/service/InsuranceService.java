package com.yuanluesoft.lss.insurance.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.lss.insurance.pojo.InsuranceUser;

/**
 * 
 * @author linchuan
 *
 */
public interface InsuranceService extends BusinessService, MemberService {
	
	/**
	 * 数据导入
	 * @param importId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void importData(long importId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 注册用户
	 * @param identityCardNumber
	 * @param name
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public InsuranceUser registUser(String identityCardNumber, String name, String password) throws ServiceException;
}