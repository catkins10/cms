package com.yuanluesoft.bidding.enterprise.actions.importdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.enterprise.services.EnterpriseImportService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class ImportData extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EnterpriseImportService enterpriseImportService = (EnterpriseImportService)getService("enterpriseImportService");
        enterpriseImportService.importData(request.getParameter("file"));
    	response.getWriter().write("import complete.");
    	return null;
    }
}