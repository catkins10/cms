package com.yuanluesoft.fet.tradestat.actions.exportquery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.tradestat.actions.FetBaseAction;
import com.yuanluesoft.fet.tradestat.forms.ExportQuery;
import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.fet.tradestat.service.TradeStatService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Download extends FetBaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(Exception se) {
    		return redirectToLogin(this, mapping, form, request, response, se, true);
    	}
    	ExportQuery exportQueryForm = (ExportQuery)form;
    	String companyCode = null;
    	String countyCode = null;
    	String countyName = null;
    	String developmentAreaCode = null;
    	int year = 0;
    	int month = 0;
		switch(sessionInfo.getUserType()) {
		case FetCompanyService.USER_TYPE_COMPANY: //企业用户
			companyCode = ((FetSessionInfo)sessionInfo).getCode();
			break;

		case FetCompanyService.USER_TYPE_COUNTY:
			countyCode = ((FetSessionInfo)sessionInfo).getCode();
			countyName = ((FetSessionInfo)sessionInfo).getUserName();
			break;

		case FetCompanyService.USER_TYPE_DEVELOPMENT_AREA:
			developmentAreaCode = ((FetSessionInfo)sessionInfo).getCode();
			break;

		default:
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), true);
		}
		try {
			year = Integer.parseInt(exportQueryForm.getQueryYear());
		}
		catch(Exception e) {

		}
		try {
			month = Integer.parseInt(exportQueryForm.getQueryMonth());
		}
		catch(Exception e) {

		}
		//输出数据
		TradeStatService tradeStatService = (TradeStatService)getService("tradeStatService");
		tradeStatService.exportExportData(ListUtils.generateList(countyCode, ","), ListUtils.generateList(countyName, ","), ListUtils.generateList(developmentAreaCode, ","), null, companyCode, exportQueryForm.getQueryCompany(), year, month, response);
		return null;
    }
}