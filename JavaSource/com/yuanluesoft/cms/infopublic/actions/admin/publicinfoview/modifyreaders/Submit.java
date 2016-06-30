package com.yuanluesoft.cms.infopublic.actions.admin.publicinfoview.modifyreaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.actions.admin.publicinfoview.PublicInfoViewAction;
import com.yuanluesoft.cms.infopublic.forms.admin.PublicInfoView;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends PublicInfoViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		PublicInfoView publicInfoViewForm = (PublicInfoView)viewForm;
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		if(!publicDirectoryService.checkPopedom(publicInfoViewForm.getDirectoryId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
		publicInfoService.modifyReaders(viewForm.getViewPackage().getView(), viewForm.getViewPackage().getCategories(), viewForm.getViewPackage().getSearchConditions(), viewForm.getViewPackage().getSelectedIds(), request.getParameter("modifyMode"), "true".equals(request.getParameter("selectedInfoOnly")), "true".equals(request.getParameter("deleteNotDirectoryVisitor")), request.getParameter("readerIds"), request, sessionInfo);
		viewForm.getViewPackage().setSelectedIds(null);
		return mapping.getInputForward();
	}
}