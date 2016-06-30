package com.yuanluesoft.enterprise.iso.service;

import com.yuanluesoft.enterprise.iso.pojo.IsoDocument;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface IsoDocumentService extends BusinessService {
	
	/**
	 * 获取文件
	 * @param documentId
	 * @return
	 * @throws ServiceException
	 */
	public IsoDocument getDocument(long documentId) throws ServiceException;
	
	/**
	 * 创建修改记录
	 * @param sourceDocumentId
	 * @param sessionInfo
	 * @return
	 */
	public IsoDocument createModify(long sourceDocumentId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建销毁记录
	 * @param sourceDocumentId
	 * @param sessionInfo
	 * @return
	 */
	public IsoDocument createDestroy(long sourceDocumentId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 完成登记
	 * @param document
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public void completeCreate(IsoDocument document, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 完成修改
	 * @param documentModify
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeModify(IsoDocument documentModify, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 完成销毁
	 * @param documentDestory
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeDestroy(IsoDocument documentDestory, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 更新文件隶属目录
	 * @param document
	 * @param newDocument
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	public void updateDocumentSubjections(IsoDocument document, boolean newDocument, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 获取初始的版本号
	 * @return
	 */
	public double getVersionInitialValue();
}