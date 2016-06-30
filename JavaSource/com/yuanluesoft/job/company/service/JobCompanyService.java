package com.yuanluesoft.job.company.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.pojo.JobCompany;

/**
 * 
 * @author linchuan
 *
 */
public interface JobCompanyService extends BusinessService, MemberService {
	public static final int PERSON_TYPE_JOB_COMPANY = 100;

	/**
	 * 更新企业所在行业
	 * @param company
	 * @param industryIds
	 * @param industryNames
	 * @throws ServiceException
	 */
	public void updateCompanyIndustries(JobCompany company, String industryIds, String industryNames) throws ServiceException;
	
	/**
	 * 企业审核
	 * @param company
	 * @param pass
	 * @param failedReason
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalCompany(JobCompany company, boolean pass, String failedReason, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 刷新职位
	 * @param job
	 * @throws ServiceException
	 */
	public void refreshJob(Job job) throws ServiceException;
	
	/**
	 * 获取剩余刷新次数
	 * @param companyId
	 * @return
	 * @throws ServiceException
	 */
	public int getRefreshTimesLeft(long companyId) throws ServiceException;
	
	/**
	 * 获取最后刷新时间
	 * @param companyId
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp getLastRefreshTime(long companyId) throws ServiceException;
	
	/**
	 * 刷新所有有效职位
	 * @param companyId
	 * @throws ServiceException
	 */
	public void refreshAllJobs(long companyId) throws ServiceException;
	
	/**
	 * 职位推送
	 * @param job
	 * @param mailSubject
	 * @param mailContent
	 * @param receiverIds
	 * @param receivers
	 * @param sessionInfo
	 * @param request
	 * @throws ServiceException
	 */
	public void pushJob(Job job, String mailSubject, String mailContent, String receiverIds, String receivers, SessionInfo sessionInfo, HttpServletRequest request) throws ServiceException;
}