package com.yuanluesoft.qualification.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.qualification.pojo.Qualification;

public interface QualificationService extends BusinessService {
	/**
	 * 审核证书
	 * @param qualification
	 * @param pass
	 * @throws ServiceException
	 */
	public void approvalQualification(Qualification qualification, boolean pass) throws ServiceException;
}
