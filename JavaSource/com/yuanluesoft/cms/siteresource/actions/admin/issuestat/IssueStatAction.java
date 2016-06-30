package com.yuanluesoft.cms.siteresource.actions.admin.issuestat;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.siteresource.forms.IssueStat;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class IssueStatAction extends ViewFormAction {
     
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/stat";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "siteResourceStat";
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		IssueStat issueStatForm = (IssueStat)viewForm;
		if(issueStatForm.getSiteIds()!=null && !issueStatForm.getSiteIds().isEmpty()) { //指定栏目
	    	view.addJoin(", WebDirectorySubjection WebDirectorySubjection");
			String where = "subjections.siteId=WebDirectorySubjection.directoryId" +
						   " and WebDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(issueStatForm.getSiteIds()) + ")";
			view.addWhere(where);
		}
    }
}