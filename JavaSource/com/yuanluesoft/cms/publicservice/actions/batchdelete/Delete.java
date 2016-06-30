package com.yuanluesoft.cms.publicservice.actions.batchdelete;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.application.forms.ApplicationView;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ApplicationView applicationViewForm = (ApplicationView)viewForm;
		if(!getAcl(applicationViewForm.getApplicationName(), sessionInfo).contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			throw new PrivilegeException();
		}
		//获取业务逻辑定义
		BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(viewForm.getViewPackage().getView().getPojoClassName());
		//获取业务逻辑服务
		String serviceName = (businessObject==null || businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		BusinessService businessService = (BusinessService)getService(serviceName);
		//获取流程服务
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				PublicService publicService = (PublicService)businessService.load(PublicService.class, Long.parseLong(ids[i]));
				workflowExploitService.suspendWorkflowInstance(publicService.getWorkflowInstanceId(), publicService, sessionInfo); //挂起流程
				publicService.setIsDeleted(1);
				businessService.update(publicService);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		viewForm.getViewPackage().setSelectedIds(null);
		return mapping.getInputForward();
	}
}