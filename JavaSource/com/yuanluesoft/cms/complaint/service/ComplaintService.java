package com.yuanluesoft.cms.complaint.service;

import java.util.List;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface ComplaintService extends PublicService {

	/**
	 * 按站点获取诉求件类型列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listTypes(long siteId) throws ServiceException;
}