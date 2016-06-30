package com.yuanluesoft.j2oa.info.service.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class InfoViewServiceImpl extends ViewServiceImpl {
	private InfoService infoService; //信息采编服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, int, boolean, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		if("issuedInfo".equals(view.getName())) { //已处理稿件
			if(!infoService.isMagazineEditor(sessionInfo)) {
				ListUtils.removeObjectByProperty(view.getActions(), "title", "补录");
			}
		}
		else if(beginRow==0) {
			infoService.synchContributeInfos();
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/**
	 * @return the infoService
	 */
	public InfoService getInfoService() {
		return infoService;
	}

	/**
	 * @param infoService the infoService to set
	 */
	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}

}