package com.yuanluesoft.municipal.facilities.service.spring;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.municipal.facilities.pojo.PdaUser;
import com.yuanluesoft.municipal.facilities.service.FacilitiesService;

/**
 * 
 * @author linchuan
 *
 */
public class PdaUserViewServiceImpl extends ViewServiceImpl {
	private FacilitiesService facilitiesService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if(records==null || records.isEmpty()) {
			return records;
		}
		for(int i=0; i<records.size(); i++) {
			PdaUser pdaUser = (PdaUser)records.get(i);
			try {
				pdaUser.setLogin(facilitiesService.isPadUserLogin(pdaUser.getCode())); //检查是否登录
				pdaUser.setGpsLogin(facilitiesService.isPadUserGPSLogin(pdaUser.getCode())); //检查是否登录GPS
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		Collections.sort(records, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				PdaUser pdaUser0 = (PdaUser)arg0;
				PdaUser pdaUser1 = (PdaUser)arg1;
				if(pdaUser0.isGpsLogin() && !pdaUser1.isGpsLogin()) {
					return -1;
				}
				else if(!pdaUser0.isGpsLogin() && pdaUser1.isGpsLogin()) {
					return 1;
				}
				else if(pdaUser0.isLogin() && !pdaUser1.isLogin()) {
					return -1;
				}
				else if(!pdaUser0.isLogin() && pdaUser1.isLogin()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
		return records;
	}

	/**
	 * @return the facilitiesService
	 */
	public FacilitiesService getFacilitiesService() {
		return facilitiesService;
	}

	/**
	 * @param facilitiesService the facilitiesService to set
	 */
	public void setFacilitiesService(FacilitiesService facilitiesService) {
		this.facilitiesService = facilitiesService;
	}
}