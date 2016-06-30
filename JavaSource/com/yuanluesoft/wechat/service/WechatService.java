package com.yuanluesoft.wechat.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.wechat.pojo.WechatAccount;
import com.yuanluesoft.wechat.pojo.WechatMessageSend;
import com.yuanluesoft.wechat.pojo.WechatWorkflowConfig;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 *
 */
public interface WechatService extends BusinessService, WorkflowConfigureCallback {
	
	/**
	 * 处理接收到的信息
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void processReceivedMessage(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 按单位ID获取微信公众号配置
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	public WechatAccount getWechatAccountByUnitId(long unitId) throws ServiceException;
	
	/**
	 * 构造菜单树
	 * @param account
	 * @return
	 * @throws ServiceException
	 */
	public Tree createMenuTree(WechatAccount account) throws ServiceException;
	
	/**
	 * 获取工作流配置
	 * @param orgId
	 * @param inheritParentEnabled 是否允许继承上级的流程
	 * @return
	 * @throws ServiceException
	 */
	public WechatWorkflowConfig getWorkflowConfig(long orgId, boolean inheritParentEnabled) throws ServiceException;
	
	/**
	 * 保存或更新分组成员
	 * @param groupId
	 * @param memberIds
	 * @throws ServiceException
	 */
	public void saveUserGroupMembers(long groupId, String memberIds) throws ServiceException;
	
	/**
	 * 发布消息
	 * @param messageSend
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issue(WechatMessageSend messageSend, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建微信图文消息,返回URL
	 * @param newsList
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public String createWechatMessageNews(List newsList,  SessionInfo sessionInfo) throws ServiceException;
}