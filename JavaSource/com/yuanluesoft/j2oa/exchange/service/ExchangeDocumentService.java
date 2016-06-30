package com.yuanluesoft.j2oa.exchange.service;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface ExchangeDocumentService extends BusinessService {
	
	/**
	 * 按源记录ID获取文档
	 * @param sourceRecordId
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	public ExchangeDocument getDocumentBySourceRecordId(String sourceRecordId, long unitId) throws ServiceException;
	
	/**
	 * 发布
	 * @param document
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issueDocument(ExchangeDocument document, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param document
	 * @param reason
	 * @param resign
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void unissueDocument(ExchangeDocument document, String reason, boolean resign, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 签收
	 * @param document
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void signDocument(ExchangeDocument document, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取答复信息
	 * @param messageId
	 * @return
	 * @throws ServiceException
	 */
	public ExchangeMessage getReplyMessage(long messageId) throws ServiceException;
}