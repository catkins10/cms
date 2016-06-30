package com.yuanluesoft.qualification.service.spring;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.qualification.pojo.Qualification;
import com.yuanluesoft.qualification.service.QualificationService;

public class QualificationServiceImpl extends BusinessServiceImpl implements
		QualificationService {

	/* （非 Javadoc）
	 * @see com.yuanluesoft.qualification.service.QualificationService#approvalQualification(com.yuanluesoft.qualification.pojo.Qualification, boolean)
	 */
	public void approvalQualification(Qualification qualification, boolean pass) throws ServiceException {
		// TODO 自动生成方法存根
		qualification.setApprovalPass(pass?'1':'2');
		getDatabaseService().updateRecord(qualification);
	}

}
