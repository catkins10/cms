package com.yuanluesoft.enterprise.quality.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.quality.pojo.QualityDocument;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 质量文档服务
 * @author linchuan
 *
 */
public interface QualityDocumentService extends BusinessService {

	/**
	 * 追加文档
	 * @param document
	 * @param templateId
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void appendDocument(QualityDocument document, long templateId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
}