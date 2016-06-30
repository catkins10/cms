package com.yuanluesoft.cms.advert.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advert.model.FixedAdvert;
import com.yuanluesoft.cms.advert.pojo.AdvertPut;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 广告服务
 * @author linchuan
 *
 */
public interface AdvertService extends BusinessService {

	/**
	 * 获取当前投放的广告
	 * @param spaceId
	 * @return
	 * @throws ServiceException
	 */
	public AdvertPut getCurrentAdvertPut(long spaceId) throws ServiceException;
	
	/**
	 * 使用JS输出浮动广告列表
	 * @param siteId
	 * @param applicationName
	 * @param pageName
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void loadFloatAdvertsAsJs(long siteId, String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 获取固定位置广告
	 * @param advertSpaceId
	 * @return
	 * @throws ServiceException
	 */
	public FixedAdvert loadFixedAdvert(long advertSpaceId) throws ServiceException;
	
	/**
	 * 打开广告,返回广告链接地址
	 * @param advertPutId
	 * @return
	 * @throws ServiceException
	 */
	public String openAdvert(long advertPutId) throws ServiceException;
	
	/**
	 * 获取广告位列表
	 * @param siteId
	 * @param floatOnly
	 * @param fixedOnly
	 * @return
	 * @throws ServiceException
	 */
	public List listAdvertSpaces(long siteId, boolean floatOnly, boolean fixedOnly) throws ServiceException;
}