package com.yuanluesoft.cms.onlineservice.faq.service;

import java.util.List;

import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface OnlineServiceFaqService extends BusinessService {
	//状态
	public final static char FAQ_STATUS_UNISSUE = '0'; //撤销发布
	public final static char FAQ_STATUS_UNDO = '1'; //退回、取回修改
	public final static char FAQ_STATUS_TODO = '2'; //待处理
	public final static char FAQ_STATUS_ISSUE = '3'; //已发布
	public final static char FAQ_STATUS_NOPASS = '4'; //办结未发布
	public final static char FAQ_STATUS_DELETED = '5'; //已删除

	/**
	 * 发布
	 * @param faq
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issue(OnlineServiceFaq faq, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param faq
	 * @throws ServiceException
	 */
	public void unissue(OnlineServiceFaq faq) throws ServiceException;
	
	/**
	 * 保存或者更新FAQ隶属目录和隶属事项
	 * @param faq
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @param subjectionItemIds
	 * @param subjectionItemNames
	 * @throws ServiceException
	 */
	public void updateFaqSubjectios(OnlineServiceFaq faq, boolean isNew, String subjectionDirectoryIds, String subjectionItemIds, String subjectionItemNames) throws ServiceException;
	
	/**
	 * 获取管理问题列表
	 * @param faqId
	 * @param publicOnly 是否只获取已经公开的
	 * @return
	 * @throws ServiceException
	 */
	public List listRelationFaqs(long faqId, boolean publicOnly) throws ServiceException;
	
	/**
	 * 添加相关问题 
	 * @param faq
	 * @param newRelationFaqIds
	 * @throws ServiceException
	 */
	public void addRelationFaqs(OnlineServiceFaq faq, String newRelationFaqIds) throws ServiceException;
	
	/**
	 * 删除相关问题
	 * @param faq
	 * @param selectedRelationIds
	 * @throws ServiceException
	 */
	public void removeRelationFaqs(OnlineServiceFaq faq, String[] selectedRelationFaqIds) throws ServiceException;
	
	/**
	 * 按目录获取问题列表,不含子目录
	 * @param directoryId
	 * @param publicOnly 是否只获取已经公开的
	 * @return
	 * @throws ServiceException
	 */
	public List listFaqs(long directoryId, boolean publicOnly) throws ServiceException;
}