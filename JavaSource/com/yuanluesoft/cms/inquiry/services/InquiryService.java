package com.yuanluesoft.cms.inquiry.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.inquiry.model.InquiryVoterTotal;
import com.yuanluesoft.cms.inquiry.pojo.InquiryFeedback;
import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface InquiryService extends BusinessService {
		
	/**
	 * 获取调查列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listInquirySubjects(long siteId) throws ServiceException;
	
	/**
	 * 提交调查结果
	 * @param inquiryId
	 * @param selectedOptions
	 * @param ip
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void submitInquiryBySingleOption(long inquiryId, long optionId, String ip, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 提交调查结果
	 * @param inquiryId
	 * @param selectedOptions
	 * @param ip
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void submitInquiry(long inquiryId, String selectedOptions, String ip, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 是否匿名调查
	 * @param inquiryIds
	 * @return
	 * @throws ServiceException
	 */
	public boolean isAnonymous(String inquiryIds) throws ServiceException;
	
	/**
	 * 是否匿名调查
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isAnonymous(long subjectId) throws ServiceException;
	
	/**
	 * 是否隐藏调查结果
	 * @param inquiryIds
	 * @return
	 * @throws ServiceException
	 */
	public boolean isHideResult(String inquiryIds) throws ServiceException;
	
	/**
	 * 是否有问卷
	 * @param inquiryIds
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasQuestionnaire(String inquiryIds) throws ServiceException;
	
	/**
	 * 是否问卷
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isQuestionnaire(long subjectId) throws ServiceException;
	
	/**
	 * 获取用户投的选项ID
	 * @param inquiryId
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public List listInquiryVotes(long subjectId, long personId) throws ServiceException;
	
	/**
	 * 参与情况统计
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public InquiryVoterTotal totalInquiryVoter(long subjectId) throws ServiceException;
	
	/**
	 * 获取投票结果
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public void retrieveInquiryResults(InquirySubject inquirySubject) throws ServiceException;
	
	/**
	 * 获取投票匹配情况,调用前必须先执行retrieveInquiryResults
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public void retrieveInquiryMatchs(InquirySubject inquirySubject) throws ServiceException;
	
	/**
	 * 获取一组调查的投票结果
	 * @param inquiryIds
	 * @return
	 * @throws ServiceException
	 */
	public List retrieveInquiryResults(String inquiryIds) throws ServiceException;
	
	/**
	 * 设置投票数
	 * @param optionId
	 * @param count
	 * @return
	 * @throws ServiceException
	 */
	public void setVoteNumber(long optionId, int count) throws ServiceException;
	
	/**
	 * 是否强制密码校验
	 * @return
	 */
	public boolean isForceValidateCode();
	
	/**
	 * 是否有结果反馈
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasFeedback(long subjectId) throws ServiceException;
	
	/**
	 * 按调查ID获取结果反馈
	 * @param inquiryIds
	 * @return
	 * @throws ServiceException
	 */
	public InquiryFeedback getInquiryFeedback(String inquiryIds) throws ServiceException;
	
	/**
	 * 按主题ID获取结果反馈
	 * @param subjectId
	 * @return
	 * @throws ServiceException
	 */
	public InquiryFeedback getInquiryFeedback(long subjectId) throws ServiceException;
	
	/**
	 * 输出投票结果图标
	 * @param inquiryId
	 * @param chartWidth
	 * @param chartHeight
	 * @param chartMode
	 * @param response
	 * @throws ServiceException
	 */
	public void writeInquiryResultChart(long inquiryId, int chartWidth, int chartHeight, String chartMode, HttpServletResponse response) throws ServiceException;
}