package com.yuanluesoft.traffic.busline.pages;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.traffic.busline.pojo.BusLine;
import com.yuanluesoft.traffic.busline.service.BusLineService;

/**
 * 
 * @author linchuan
 *
 */
public class BusLinePageService extends PageService {
	private BusLineService busLineService; //公交线路服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		Object record = sitePage.getAttribute("record");
		if(record instanceof BusLine) {
			BusLine busLine = (BusLine)record;
			Date changeEndDate = busLineService.resetBusLineByInterimChange(busLine);
			if(changeEndDate!=null) {
				//设置页面有效时间
				PageUtils.setPageExpiresTime(DateTimeUtils.add(changeEndDate, Calendar.DAY_OF_MONTH, 1), request);
			}
		}
	}

	/**
	 * @return the busLineService
	 */
	public BusLineService getBusLineService() {
		return busLineService;
	}

	/**
	 * @param busLineService the busLineService to set
	 */
	public void setBusLineService(BusLineService busLineService) {
		this.busLineService = busLineService;
	}

}