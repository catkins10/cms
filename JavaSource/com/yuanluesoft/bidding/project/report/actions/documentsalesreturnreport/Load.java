package com.yuanluesoft.bidding.project.report.actions.documentsalesreturnreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.report.actions.agentsalesreport.AgentSalesReportAction;

/**
 * 
 * @author lmiky
 *
 */
public class Load extends AgentSalesReportAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeLoadAction(mapping, form, request, response);
	}
}
