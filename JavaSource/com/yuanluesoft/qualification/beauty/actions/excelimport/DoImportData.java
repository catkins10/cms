package com.yuanluesoft.qualification.beauty.actions.excelimport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



public class DoImportData extends ImportDataAction {
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	        return executeSaveAction(mapping, form, request, response, false, null, "导入成功", null);
	    }
}
