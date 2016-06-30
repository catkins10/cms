package com.yuanluesoft.msa.lawmen.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface MsaLawmenService extends BusinessService {

	/**
	 * 数据导入
	 * @param importId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public void importData(long importId, SessionInfo sessionInfo) throws ServiceException;
}