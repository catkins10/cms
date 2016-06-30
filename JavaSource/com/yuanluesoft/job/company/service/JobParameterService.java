package com.yuanluesoft.job.company.service;

import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.job.company.pojo.JobCompanyApproval;
import com.yuanluesoft.job.company.pojo.JobCompanyMailTemplate;
import com.yuanluesoft.job.talent.pojo.JobTalentApproval;

/**
 * 
 * @author linchuan
 *
 */
public interface JobParameterService extends DirectoryService {

	/**
	 * 获取企业审核设置
	 * @return
	 * @throws ServiceException
	 */
	public JobCompanyApproval getJobCompanyApproval() throws ServiceException;
	
	/**
	 * 获取人才审核设置
	 * @return
	 * @throws ServiceException
	 */
	public JobTalentApproval getJobTalentApproval() throws ServiceException;
	
	/**
	 * 按企业ID获取邮件模板
	 * @param companyId
	 * @return
	 * @throws ServiceException
	 */
	public JobCompanyMailTemplate getMailTemplateByCompanyId(long companyId) throws ServiceException;
	
	/**
	 * 获取职位刷新次数限制
	 * @return
	 */
	public int getJobRefreshLimitation();
}