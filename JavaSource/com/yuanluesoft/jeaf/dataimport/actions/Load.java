package com.yuanluesoft.jeaf.dataimport.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends ImportDataAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			checkPrivilege(request, response);
		}
		catch(Exception e) {
			return redirectToLogin(this, mapping, form, request, response, e, true);
		}
		com.yuanluesoft.jeaf.dataimport.forms.ImportData importDataForm = (com.yuanluesoft.jeaf.dataimport.forms.ImportData)form;
		if(importDataForm.getTargetSiteName()!=null && !importDataForm.getTargetSiteName().isEmpty() &&
		   importDataForm.getDataImportServiceClass()!=null && !importDataForm.getDataImportServiceClass().isEmpty()) {
			//加载导入参数配置
			DataImportParameterService dataImportParameterService = (DataImportParameterService)getService("dataImportParameterService");
			importDataForm.setParameter(dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass()));
			importDataForm.setSubForm("importDataParameter.jsp");
		}
		else {
			importDataForm.setSubForm("importDataTargetSite.jsp");
		}
		return mapping.findForward("load");
    }
}