package com.yuanluesoft.jeaf.dataimport.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SaveDataTree extends ImportDataAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			checkPrivilege(request, response);
		}
		catch(Exception e) {
			return redirectToLogin(this, mapping, form, request, response, e, true);
		}
		com.yuanluesoft.jeaf.dataimport.forms.ImportData importDataForm = (com.yuanluesoft.jeaf.dataimport.forms.ImportData)form;
		//加载导入参数配置
		DataImportParameterService dataImportParameterService = (DataImportParameterService)getService("dataImportParameterService");
		importDataForm.setParameter(dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass()));
		//获取数据导入服务
		DataImportService dataImportService = getDataImportService(importDataForm.getDataImportServiceClass());
		//保存目录树
		dataImportService.saveDataDirectoryTrees(request, importDataForm.getTargetSiteId(), dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass()));
		//重新载入目录树
		importDataForm.setDataDirectoryTrees(dataImportService.listDataDirectoryTrees(importDataForm.getTargetSiteId(), dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass())));
		importDataForm.setSubForm("importDataTree.jsp");
		return mapping.getInputForward();
    }
}