package com.yuanluesoft.cms.publicservice.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.capture.service.CaptureRecordService;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceSms;
import com.yuanluesoft.cms.publicservice.pojo.WorkflowSetting;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 *
 */
public interface PublicService extends BusinessService, SmsContentService, WorkflowConfigureCallback, CaptureRecordService {
	
	/**
	 * 获取编号
	 * @return
	 * @throws ServiceException
	 */
	public String getSN() throws ServiceException;
	
	/**
	 * 根据编号和查询密码获取公众服务信息
	 * @param pojoClassName
	 * @param sn
	 * @param queryPassword
	 * @return
	 * @throws ServiceException
	 */
	public com.yuanluesoft.cms.publicservice.pojo.PublicService loadPublicService(String pojoClassName, String sn, String queryPassword) throws ServiceException;
	
	/**
	 * 获取审批流程配置
	 * @param applicationName
	 * @param siteId
	 * @param inheritParentEnabled 是否允许继承父站点的配置
	 * @return
	 * @throws ServiceException
	 */
	public WorkflowSetting getWorkflowSetting(String applicationName, long siteId, boolean inheritParentEnabled) throws ServiceException;
	
	/**
	 * 发送办理情况短信给提交人
	 * @param contentName 内容名称,如：信件、投诉或建议
	 * @param publicServiceId
	 * @throws ServiceException
	 */
	public void sendSmsToCreator(String applicationName, long publicServiceId) throws ServiceException;
	
	/**
	 * 加载办结短信格式配置
	 * @param applicationName
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public PublicServiceSms loadSmsFormatConfig(String applicationName, long siteId) throws ServiceException;
	
	/**
	 * 是否强制密码校验
	 * @return
	 */
	public boolean isForceValidateCode();
	
	/**
	 * 修改访问者
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param selectedIds
	 * @param modifyMode
	 * @param selectedOnly
	 * @param readerIds
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedIds, String modifyMode, boolean selectedOnly, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布超时的记录
	 * @throws ServiceException
	 */
	public void unpublishTimeoutRecords() throws ServiceException;
}