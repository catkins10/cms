package com.yuanluesoft.cms.photocollect.service;

import java.util.List;

import com.yuanluesoft.cms.photocollect.pojo.PhotoCollect;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface PhotoCollectService extends PublicService {

	/**
	 * 按站点获取照片分类列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listCategories(long siteId) throws ServiceException;
	
	/**
	 * 同步到网站栏目
	 * @param photoCollect
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void synchSiteColumns(PhotoCollect photoCollect, SessionInfo sessionInfo) throws ServiceException;
}