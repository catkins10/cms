package com.yuanluesoft.cms.capture.actions.capturelogview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.capture.pojo.CmsCaptureLog;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class DeleteLogs extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()!=null && !viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			CaptureService captureService = (CaptureService)getService("captureService");
			String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
			for(int i=0; i<ids.length; i++) {
				captureService.delete(captureService.load(CmsCaptureLog.class, Long.parseLong(ids[i])));
			}
			viewForm.getViewPackage().setSelectedIds(null); //清空列表
		}
		return mapping.getInputForward();
	}
}