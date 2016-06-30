/**
 * 
 */
package com.yuanluesoft.bbs.article.service;

import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * @author yuanluesoft
 *
 */
public interface BbsReplyService extends BusinessService {
	
	/**
	 * 发布
	 * @param reply
	 * @throws ServiceException
	 */
	public void press(BbsReply reply) throws ServiceException;
}
