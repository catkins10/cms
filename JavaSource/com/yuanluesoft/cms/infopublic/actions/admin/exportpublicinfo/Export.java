package com.yuanluesoft.cms.infopublic.actions.admin.exportpublicinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.forms.admin.ExportPublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;

/**
 * 
 * @author linchuan
 *
 */
public class Export extends ExportPublicInfoAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ExportPublicInfo exportPublicInfoForm = (ExportPublicInfo)form;
        PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
        publicInfoService.exportPublicInfo(0, exportPublicInfoForm.getViewPackage().getSelectedIds(), request, response);
        return null;
    }
}