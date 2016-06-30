package com.yuanluesoft.fet.tradestat.actions.exportquery;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fet.tradestat.actions.FetViewFormAction;
import com.yuanluesoft.fet.tradestat.forms.ExportQuery;
import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.fet.tradestat.service.TradeStatService;
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
		return "tradeExport";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		ExportQuery exportQueryForm = (ExportQuery)viewForm;
		String where = null;
		switch(sessionInfo.getUserType()) {
		case FetCompanyService.USER_TYPE_COMPANY: //企业用户
			where = "TradeExport.companyCode='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getCode()) + "'";
			break;

		case FetCompanyService.USER_TYPE_COUNTY:
			TradeStatService tradeStatService = (TradeStatService)getService("tradeStatService");
			if(tradeStatService.isCountyQueryByCode()) {
				where = "TradeExport.countyCode='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getCode()) + "'";
			}
			else {
				where = "TradeExport.county='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getUserName()) + "'";
			}
			break;

		case FetCompanyService.USER_TYPE_DEVELOPMENT_AREA:
			where = "TradeExport.developmentAreaCode='" + JdbcUtils.resetQuot(((FetSessionInfo)sessionInfo).getCode()) + "'";
			break;

		default:
			throw new PrivilegeException();
		}
		try {
			int year = Integer.parseInt(exportQueryForm.getQueryYear());
			where =  (where==null ? "" : where + " and ") + "TradeExport.dataYear=" + year;
		}
		catch(Exception e) {

		}
		try {
			int month = Integer.parseInt(exportQueryForm.getQueryMonth());
			where =  (where==null ? "" : where + " and ") + "TradeExport.dataMonth=" + month;
		}
		catch(Exception e) {

		}
		if(exportQueryForm.getQueryCompany()!=null && !exportQueryForm.getQueryCompany().equals("")) {
			where =  (where==null ? "" : where + " and ") + "TradeExport.companyName like '%" + JdbcUtils.resetQuot(exportQueryForm.getQueryCompany()) + "%'";
		}
		view.addWhere(where);
	}
}