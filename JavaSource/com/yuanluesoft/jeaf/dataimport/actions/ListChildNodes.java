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
 * @author linchuan
 *
 */
public class ListChildNodes extends ImportDataAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		com.yuanluesoft.jeaf.dataimport.forms.ImportData importDataForm = (com.yuanluesoft.jeaf.dataimport.forms.ImportData)form;
		DataImportParameterService dataImportParameterService = (DataImportParameterService)getService("dataImportParameterService");
		DataImportParameter parameter = dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass());
		importDataForm.setParameter(parameter);
		DataImportService dataImportService = getDataImportService(importDataForm.getDataImportServiceClass());
		importDataForm.setChildNodes(dataImportService.listDataDirectoryTreeNodes(importDataForm.getImportDataName(), importDataForm.getParentNodeId(), importDataForm.isSourceTree(), importDataForm.getTargetSiteId(), dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass())));
        importDataForm.setActionResult("SUCCESS");
    	return mapping.findForward("load");
    }
}