package com.yuanluesoft.msa.seafarer.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.msa.seafarer.pojo.MsaImportLog;

/**
 * 船员服务
 * @author linchuan
 *
 */
public interface SeafarerService extends BusinessService {

	/**
	 * 数据导入
	 * @param importId
	 * @param dataType
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public MsaImportLog importData(long importId, String dataType, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除导入的数据
	 * @param importId
	 * @param dataType
	 * @throws ServiceException
	 */
	public void deleteImportedData(long importId, String dataType) throws ServiceException;
}