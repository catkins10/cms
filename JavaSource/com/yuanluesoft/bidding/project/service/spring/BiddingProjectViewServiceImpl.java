package com.yuanluesoft.bidding.project.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 工程视图定义服务
 * @author yuanlue
 *
 */
public class BiddingProjectViewServiceImpl extends ViewServiceImpl {
	private BiddingProjectParameterService biddingProjectParameterService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewActions(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		super.retrieveViewActions(view, request, sessionInfo);
		List cities = biddingProjectParameterService.listProjectCreatableCities(sessionInfo);
		if(cities==null || cities.isEmpty()) { //没有允许项目登记的地区
			view.setActions(null);
			return;
		}
	}
	
	/**
	 * @return the biddingProjectParameterService
	 */
	public BiddingProjectParameterService getBiddingProjectParameterService() {
		return biddingProjectParameterService;
	}

	/**
	 * @param biddingProjectParameterService the biddingProjectParameterService to set
	 */
	public void setBiddingProjectParameterService(
			BiddingProjectParameterService biddingProjectParameterService) {
		this.biddingProjectParameterService = biddingProjectParameterService;
	}
}