package com.yuanluesoft.job.talent.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.job.talent.pojo.JobTalent;

/**
 * 人才服务
 * @author linchuan
 *
 */
public interface JobTalentService extends BusinessService, MemberService {
	public static final int PERSON_TYPE_JOB_TALENT = 101;
	
	/**
	 * 人才审核
	 * @param company
	 * @param pass
	 * @param failedReason
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalTalent(JobTalent talent, boolean pass, String failedReason, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 职位推荐
	 * @param talentId
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listRecommendJobs(long talentId, int limit) throws ServiceException;
	
	/**
	 * 取消订阅
	 * @param encodedTalentId
	 * @throws ServiceException
	 */
	public void cancelPush(String encodedTalentId) throws ServiceException;
	
	/**
	 * 按班级获取就业报到记录列表
	 * @param schoolClassId
	 * @param schoolClass
	 * @param graduateDate
	 * @param schoolingLength
	 * @param qualification
	 * @param specialty
	 * @param trainingMode
	 * @param reportBegin
	 * @param reportEnd
	 * @param noticeNumber
	 * @param reportNumber
	 * @return
	 * @throws ServiceException
	 */
	public List listTalentReports(long schoolClassId, String schoolClass, Date graduateDate, String schoolingLength, String qualification, String specialty, String trainingMode, Date reportBegin, Date reportEnd, long noticeNumber, long reportNumber) throws ServiceException;
	
	/**
	 * 保存就业报到记录列表
	 * @param schoolClassId
	 * @param schoolClass
	 * @param request
	 * @throws ServiceException
	 */
	public void saveTalentReports(long schoolClassId, String schoolClass, HttpServletRequest request) throws ServiceException;
}