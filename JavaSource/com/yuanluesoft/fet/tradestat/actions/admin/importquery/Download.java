package com.yuanluesoft.fet.tradestat.actions.admin.importquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.tradestat.forms.admin.ImportQuery;
import com.yuanluesoft.fet.tradestat.pojo.FetCounty;
import com.yuanluesoft.fet.tradestat.pojo.FetDevelopmentArea;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.fet.tradestat.service.TradeStatService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Download extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(Exception se) {
    		return redirectToLogin(this, mapping, form, request, response, se, true);
    	}
    	FetCompanyService fetCompanyService = (FetCompanyService)getService("fetCompanyService");
    	//获取用户有访问权限的区县编码、名称列表
    	List counties = fetCompanyService.listPermitCounties(sessionInfo);
    	List countyCodes = new ArrayList();
    	List countyNames = new ArrayList();
    	if(counties!=null) {
    		for(Iterator iterator = counties.iterator(); iterator.hasNext();) {
    			FetCounty county = (FetCounty)iterator.next();
    			countyCodes.add(county.getCode());
    			countyNames.add(county.getName());
    		}
    	}
    	//获取用户有访问权限的开发区编码、名称列表
    	List developmentAreas = fetCompanyService.listPermitDevelopmentAreas(sessionInfo);
    	List developmentAreaCodes = new ArrayList();
    	List developmentAreaNames = new ArrayList();
    	if(developmentAreas!=null) {
    		for(Iterator iterator = developmentAreas.iterator(); iterator.hasNext();) {
    			FetDevelopmentArea developmentArea = (FetDevelopmentArea)iterator.next();
    			developmentAreaCodes.add(developmentArea.getCode());
    			developmentAreaNames.add(developmentArea.getName());
    		}
    	}
    	ImportQuery importQueryForm = (ImportQuery)form;
    	int year = 0;
    	int month = 0;
		String[] categories = importQueryForm.getViewPackage().getCategories().split(",");
		try {
			year = Integer.parseInt(categories[1].split("\\x7c")[1]);
		}
		catch(Exception e) {

		}
		try {
			month = Integer.parseInt(categories[2].split("\\x7c")[1]);
		}
		catch(Exception e) {

		}
		//输出数据
		TradeStatService tradeStatService = (TradeStatService)getService("tradeStatService");
		tradeStatService.exportImportData(countyCodes, countyNames, developmentAreaCodes, developmentAreaNames, null, null, year, month, response);
		return null;
    }
}