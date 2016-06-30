package com.yuanluesoft.fet.tradestat.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author yuanluesoft
 *
 */
public abstract class FetFormAction extends FormAction {
	
	public FetFormAction() {
		super();
		sessionInfoClass = FetSessionInfo.class;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() + "/fet/tradestat/statLogin.shtml?siteId=" + request.getParameter("siteId"), "utf-8"); 
	}

	/**
	 * 加密口令
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String encodePassword(String password) throws Exception {
		if(password.startsWith("{") && password.endsWith("}")) {
			return password.substring(1, password.length() - 1);
		}
		else {
			FetCompanyService fetCompanyService = (FetCompanyService)getService("fetCompanyService");
			return fetCompanyService.cryptPassword(password);
		}
	}
}
