package com.yuanluesoft.jeaf.dataimport.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ImportData extends ImportDataAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			checkPrivilege(request, response);
		}
		catch(Exception e) {
			return redirectToLogin(this, mapping, form, request, response, e, true);
		}
		com.yuanluesoft.jeaf.dataimport.forms.ImportData importDataForm = (com.yuanluesoft.jeaf.dataimport.forms.ImportData)form;
		DataImportParameterService dataImportParameterService = (DataImportParameterService)getService("dataImportParameterService");
		DataImportParameter parameter = dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass());
		DataImportService dataImportService = getDataImportService(importDataForm.getDataImportServiceClass());
		//保存目录树
		dataImportService.saveDataDirectoryTrees(request, importDataForm.getTargetSiteId(), parameter);
		//导入数据
		dataImportService.importData(importDataForm.getTargetSiteId(), parameter);
		response.getWriter().write("import complete.");
        return null;
    }
}