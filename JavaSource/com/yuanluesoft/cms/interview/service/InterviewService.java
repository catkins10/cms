package com.yuanluesoft.cms.interview.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeak;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeakFlow;
import com.yuanluesoft.cms.interview.pojo.InterviewSubject;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 在线访谈服务
 * @author linchuan
 *
 */  
public interface InterviewService extends BusinessService, MemberService {
	//发言人类型
	public static final int SPEAKER_TYPE_COMPERE = 1; //主持人
	public static final int SPEAKER_TYPE_GUESTS = 2; //嘉宾
	public static final int SPEAKER_TYPE_NETUSER = 3; //网友
	
	/**
	 * 获取访谈主题
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public InterviewSubject getInterviewSubject(long id) throws ServiceException;

	/**
	 * 获取发言审批流程
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public InterviewSpeakFlow loadSpeakFlow(long siteId) throws ServiceException;
	
	/**
	 * 获取发言审核人角色列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listInterviewRoles(long siteId) throws ServiceException;
	
	/**
	 * 获取下载最新发言的URL
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public String getLiveSpeaksUpdateUrl(long subjectId) throws ServiceException;
	
	/**
	 * 获取下载图片列表的URL
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public String getLiveImagesUpdateUrl(long subjectId) throws ServiceException;
	
	/**
	 * 提交发言
	 * @param speak
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void submitSpeak(InterviewSpeak speak, HttpServletRequest request, InterviewSessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 审批发言
	 * @param speakId
	 * @param pass
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalSpeak(long speakId, boolean pass, InterviewSessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 结束访谈
	 * @param subjectId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void endInterview(long subjectId, InterviewSessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户的访谈角色
	 * @param interviewSubject
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public String getInterviewRole(InterviewSubject interviewSubject, SessionInfo sessionInfo) throws ServiceException;
}