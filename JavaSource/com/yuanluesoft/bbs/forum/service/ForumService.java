package com.yuanluesoft.bbs.forum.service;

import com.yuanluesoft.bbs.forum.model.Bbs;
import com.yuanluesoft.bbs.forum.model.Category;
import com.yuanluesoft.bbs.forum.model.Forum;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface ForumService extends DirectoryService, InitializeService {
	
	/**
	 * 获取论坛模型
	 * @param bbsId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Bbs getBbs(long bbsId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取版块分类模型
	 * @param categoryId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Category getCategory(long categoryId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取论坛版块模型
	 * @param forumId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Forum getForum(long forumId, SessionInfo sessionInfo) throws ServiceException;
}