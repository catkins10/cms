package com.yuanluesoft.fet.tradestat.actions.companyquery;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fet.tradestat.actions.FetViewFormAction;
import com.yuanluesoft.fet.tradestat.forms.CompanyQuery;
import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class Query extends FetViewFormAction {
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "fet/tradestat";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "company";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		CompanyQuery companyQueryForm = (CompanyQuery)viewForm;
		String where = null;
		switch(sessionInfo.getUserType()) {
		case FetCompanyService.USER_TYPE_COUNTY:
			where = "FetCompany.county='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getUserName()) + "'";
			break;

		case FetCompanyService.USER_TYPE_DEVELOPMENT_AREA:
			where = "FetCompany.developmentArea='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getUserName()) + "'";
			break;

		default:
			//检查用户的访问权限
			if(!getAccessControlService().getAcl("fet/tradestat", sessionInfo).contains("application_visitor")) {
				throw new PrivilegeException();
			}
		}
		if(companyQueryForm.getQueryCompany()!=null && !companyQueryForm.getQueryCompany().equals("")) {
			where =  (where==null ? "" : where + " and ") + "FetCompany.name like '%" + JdbcUtils.resetQuot(companyQueryForm.getQueryCompany()) + "%'";
		}
		view.addWhere(where);
	}
}