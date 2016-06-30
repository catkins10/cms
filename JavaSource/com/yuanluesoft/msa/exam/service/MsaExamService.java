package com.yuanluesoft.msa.exam.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.msa.exam.pojo.MsaExamImportLog;

/**
 * 
 * @author linchuan
 *
 */
public interface MsaExamService extends BusinessService {

	/**
	 * 数据导入
	 * @param importId
	 * @param dataType
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public MsaExamImportLog importData(long importId, String examName, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除导入的数据
	 * @param importId
	 * @throws ServiceException
	 */
	public void deleteImportedData(long importId) throws ServiceException;
}