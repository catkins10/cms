package com.yuanluesoft.enterprise.exam.question.service;

import com.yuanluesoft.enterprise.exam.question.pojo.QuestionImport;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface QuestionImportService extends BusinessService {
	
	/**
	 * 导入数据
	 * @param importLogId
	 * @param source
	 * @param description
	 * @param postIds
	 * @param posts
	 * @param knowledgeIds
	 * @param knowledges
	 * @param itemIds
	 * @param items
	 * @param remark
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public QuestionImport importData(long importLogId, String source, String description, String postIds, String posts, String knowledgeIds, String knowledges, String itemIds, String items, String remark, SessionInfo sessionInfo) throws ServiceException;
}