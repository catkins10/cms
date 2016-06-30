package com.yuanluesoft.cms.infopublic.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface PublicInfoExportService {
	
	/**
	 * 输出XML
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param outputStream
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void exportXML(View view, String currentCategories, String searchConditions, HttpServletResponse response, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
}