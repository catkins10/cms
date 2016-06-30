package com.yuanluesoft.webmail.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.webmail.service.WebMailService;
import com.yuanluesoft.webmail.util.MailReader;

/**
 * 
 * @author linchuan
 *
 */
public class WebMailViewServiceImpl extends ViewServiceImpl {
	private WebMailService webMailService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(beginRow<=1) {
			//重新同步邮件
			webMailService.synchMailList(view.getName().equals("newMail") ? MailReader.READ_LEVEL_SUBJECT_ONLY : MailReader.READ_LEVEL_SUBJECT_AND_BODY, sessionInfo);
		}
		return super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
	}

	/**
	 * @return the webMailService
	 */
	public WebMailService getWebMailService() {
		return webMailService;
	}

	/**
	 * @param webMailService the webMailService to set
	 */
	public void setWebMailService(WebMailService webMailService) {
		this.webMailService = webMailService;
	}
}
