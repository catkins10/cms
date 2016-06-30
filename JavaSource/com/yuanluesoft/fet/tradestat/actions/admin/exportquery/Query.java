package com.yuanluesoft.fet.tradestat.actions.admin.exportquery;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fet.tradestat.service.TradeStatService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class Query extends ViewFormAction {
    
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
		return "admin/tradeExport";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		String where = null;
		TradeStatService tradeStatService = (TradeStatService)getService("tradeStatService");
		if(tradeStatService.isCountyQueryByCode()) {
			where = "TradeExport.countyCode in" +
					" (select FetCounty.code from FetCounty FetCounty, FetCountyPrivilege FetCountyPrivilege" +
					"   where FetCounty.id=FetCountyPrivilege.recordId" +
					"   and FetCountyPrivilege.visitorId in (" + sessionInfo.getUserIds() + "))" +
					" or TradeExport.developmentAreaCode in" +
					" (select FetDevelopmentArea.code from FetDevelopmentArea FetDevelopmentArea, FetDevelopmentAreaPrivilege FetDevelopmentAreaPrivilege" +
					"   where FetDevelopmentArea.id=FetDevelopmentAreaPrivilege.recordId" +
					"   and FetDevelopmentAreaPrivilege.visitorId in (" + sessionInfo.getUserIds() + "))";
			
		}
		else {
			where = "TradeExport.county in" +
					" (select FetCounty.name from FetCounty FetCounty, FetCountyPrivilege FetCountyPrivilege" +
					"   where FetCounty.id=FetCountyPrivilege.recordId" +
					"   and FetCountyPrivilege.visitorId in (" + sessionInfo.getUserIds() + "))";
		}
		view.addWhere(where);
	}
}