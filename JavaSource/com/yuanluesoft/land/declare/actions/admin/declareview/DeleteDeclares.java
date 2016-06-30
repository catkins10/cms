package com.yuanluesoft.land.declare.actions.admin.declareview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.land.declare.service.LandDeclareService;

/**
 * 
 * @author linchuan
 *
 */
public class DeleteDeclares extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()!=null && !viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			LandDeclareService landDeclareService = (LandDeclareService)getService("landDeclareService");
			String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
			for(int i=0; i<ids.length; i++) {
				landDeclareService.delete(landDeclareService.load(Class.forName(viewForm.getViewPackage().getView().getPojoClassName()), Long.parseLong(ids[i])));
			}
			viewForm.getViewPackage().setSelectedIds(null); //清空列表
		}
		return mapping.getInputForward();
	}
}