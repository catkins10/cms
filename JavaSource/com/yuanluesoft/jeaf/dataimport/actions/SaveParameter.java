package com.yuanluesoft.jeaf.dataimport.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 保存导入参数配置
 * @author linchuan
 *
 */
public class SaveParameter extends ImportDataAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			checkPrivilege(request, response);
		}
		catch(Exception e) {
			return redirectToLogin(this, mapping, form, request, response, e, true);
		}
		com.yuanluesoft.jeaf.dataimport.forms.ImportData importDataForm = (com.yuanluesoft.jeaf.dataimport.forms.ImportData)form;
		DataImportParameterService dataImportParameterService = (DataImportParameterService)getService("dataImportParameterService");
		dataImportParameterService.saveDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass(), importDataForm.getParameter()); //保存参数配置
		//获取数据导入服务
		DataImportService dataImportService = getDataImportService(importDataForm.getDataImportServiceClass());
		//获取目录树列表
		importDataForm.setDataDirectoryTrees(dataImportService.listDataDirectoryTrees(importDataForm.getTargetSiteId(), dataImportParameterService.loadDataImportParameter(importDataForm.getTargetSiteId(), importDataForm.getDataImportServiceClass())));
		importDataForm.setSubForm("importDataTree.jsp");
		return mapping.getInputForward();
    }
}