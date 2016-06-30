package com.yuanluesoft.cms.infopublic.actions.admin.exportpublicinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.forms.admin.ExportPublicInfo;
import com.yuanluesoft.jeaf.view.model.ViewPackage;

/**
 * 
 * @author linchuan
 *
 */
public class ListExportPublicInfos extends ExportPublicInfoAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ExportPublicInfo exportForm = (ExportPublicInfo)form;
    	ViewPackage viewPackage = exportForm.getViewPackage();
    	viewPackage.setSelectedIds(null);
    	viewPackage.setRecordCount(0);
    	viewPackage.setCurPage(1);
        super.execute(mapping, form, request, response);
        return mapping.getInputForward();
    }
}