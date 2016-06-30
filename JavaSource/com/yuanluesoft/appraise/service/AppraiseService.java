package com.yuanluesoft.appraise.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.model.AppraiseVoteTotal;
import com.yuanluesoft.appraise.pojo.Appraise;
import com.yuanluesoft.appraise.pojo.AppraiseParameter;
import com.yuanluesoft.appraise.pojo.AppraiseTask;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.service.SmsServiceListener;

/**
 * 
 * @author linchuan
 *
 */
public interface AppraiseService extends BusinessService, SmsServiceListener {
	
	/**
	 * 启动评议
	 * @param name
	 * @param taskId
	 * @param year
	 * @param month
	 * @param endTime 截止时间,为空时,系统自己计算截止时间
	 * @param monthOnceOnly 是否一个月只允许评议一次
	 * @param cancelNoAppraisersUnit //是否不考核没有评议员的单位
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Appraise startupAppraise(String name, long taskId, int year, int month, Timestamp endTime, boolean monthOnceOnly, boolean cancelNoAppraisersUnit, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 补发短信
	 * @param appraiseId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void supplySendAppraiseSms(long appraiseId, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 获取评议参数配置
	 * @return
	 * @throws ServiceException
	 */
	public AppraiseParameter getAppraiseParameter() throws ServiceException;
	
	/**
	 * 获取参评单位(ParticipateUnit)列表
	 * @param areaId
	 * @param year
	 * @return
	 * @throws ServiceException
	 */
	public List listParticipateUnits(long areaId, int year) throws ServiceException;
	
	/**
	 * 根据评议验证码加载评议
	 * @param appraiserNumber
	 * @param appraiseCode
	 * @param forWapAppraise 是否WAP评议
	 * @return
	 * @throws ServiceException
	 */
	public Appraise loadAppraiseByCode(String  appraiserNumber, String appraiseCode, boolean forWapAppraise) throws ServiceException;
	
	/**
	 * 提交网络评议
	 * @param appraiserNumber
	 * @param appraiseCode
	 * @param wapAppraise
	 * @param request
	 * @throws ServiceException
	 */
	public void submitInternetAppraise(String  appraiserNumber, String appraiseCode, boolean wapAppraise, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 发送短信给参评单位
	 * @param areaId
	 * @param unitIds
	 * @param message
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void sendAppraiseUnitSms(long areaId, String unitIds, String message, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 执行定时任务,每小时
	 * @throws ServiceException
	 */
	public void schedule() throws ServiceException;
	
	/**
	 * 投票统计
	 * @param areaId
	 * @param currentYearOnly
	 * @return
	 * @throws ServiceException
	 */
	public AppraiseVoteTotal appraiseVoteTotal(long areaId, boolean currentYearOnly) throws ServiceException;
	
	/**
	 * 生成评议名称
	 * @param task
	 * @param appraiseYear
	 * @param appraiseMonth
	 * @return
	 */
	public String generateAppraiseName(AppraiseTask task, int appraiseYear, int appraiseMonth);
}