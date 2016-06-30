package com.yuanluesoft.microblog.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.microblog.pojo.Microblog;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;
import com.yuanluesoft.microblog.pojo.MicroblogWorkflowConfig;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 *
 */
public interface MicroblogService extends BusinessService, WorkflowConfigureCallback {
	public final static String MICROBLOG_ISSUE_RANGE_ALL = "all"; //所有人
	public final static String MICROBLOG_ISSUE_RANGE_SELF = "self"; //仅自己
	public final static String MICROBLOG_ISSUE_RANGE_GROUPS = "groups"; //指定分组
	
	/**
	 * 获取微博平台列表
	 * @return
	 */
	public List getPlatforms();
	
	/**
	 * 获取微博帐号参数列表
	 * @param platformName
	 * @param account
	 * @return
	 * @throws ServiceException
	 */
	public Set listMicroblogAccountParameters(String platformName, MicroblogAccount account) throws ServiceException;
	
	/**
	 * 保存微博帐号参数列表
	 * @param account
	 * @param parameterValues
	 * @throws ServiceException
	 */
	public void saveMicroblogAccountParameters(MicroblogAccount account, String[] parameterValues) throws ServiceException;

	/**
	 * 获取工作流配置
	 * @param orgId
	 * @param inheritParentEnabled 是否允许继承上级的流程
	 * @return
	 * @throws ServiceException
	 */
	public MicroblogWorkflowConfig getWorkflowConfig(long orgId, boolean inheritParentEnabled) throws ServiceException;
	
	/**
	 * 获取帐号列表
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	public List listAccounts(long unitId) throws ServiceException;
	
	/**
	 * 发布微博
	 * @param microblog
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issue(Microblog microblog, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param microblog
	 * @throws ServiceException
	 */
	public void unissue(Microblog microblog) throws ServiceException;
	
	/**
	 * 读取微博列表
	 * @param microblogAccountId
	 * @param count
	 * @return
	 * @throws ServiceException
	 */
	public List readMicroblogs(long microblogAccountId, int count) throws ServiceException;
	
	/**
	 * 获取短链接的最大长度
	 * @return
	 * @throws ServiceException
	 */
	public int getShortUrlMaxLength() throws ServiceException;
	
	/**
	 * 处理接收到的消息
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void processReceivedMessage(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}