package com.yuanluesoft.fet.tradestat.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;

/**
 * 
 * @author linchuan
 *
 */
public abstract class FetViewFormAction extends ViewFormAction {
		
	public FetViewFormAction() {
		super();
		sessionInfoClass = FetSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() +  "/fet/tradestat/statLogin.shtml?siteId=" + request.getParameter("siteId"), "utf-8"); 
	}
}
