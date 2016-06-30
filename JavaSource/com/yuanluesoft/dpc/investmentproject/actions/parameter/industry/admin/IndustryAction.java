package com.yuanluesoft.dpc.investmentproject.actions.parameter.industry.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.dpc.investmentproject.actions.parameter.admin.ParameterAction;
import com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class IndustryAction extends ParameterAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		InvestmentProjectService investmentProjectService = (InvestmentProjectService)getService("investmentProjectService");
		return (Record)ListUtils.findObjectByProperty(investmentProjectService.listIndustries(), "id", PropertyUtils.getProperty(form, componentName + ".id"));
	}
}