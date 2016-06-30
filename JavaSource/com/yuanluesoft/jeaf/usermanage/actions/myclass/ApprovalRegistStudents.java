package com.yuanluesoft.jeaf.usermanage.actions.myclass;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;

/**
 * 
 * @author linchuan
 *
 */
public class ApprovalRegistStudents extends ListToApprovalStudents {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.jeaf.usermanage.forms.ListToApprovalStudents approvalStudentsForm = (com.yuanluesoft.jeaf.usermanage.forms.ListToApprovalStudents)viewForm;
    	//审批
    	String registStudentIds = approvalStudentsForm.getViewPackage().getSelectedIds();
    	if(registStudentIds!=null && !registStudentIds.equals("")) {
		   	RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
		   	//获取我任班主任的班级
			List classesInCharge = getOrgService().listClassesInCharge(sessionInfo.getUserId());
			if(classesInCharge==null || classesInCharge.isEmpty()) {
	    		throw new PrivilegeException();
	    	}
			long classId = ((Org)classesInCharge.get(0)).getId();
			String[] ids = registStudentIds.split(",");
			for(int i=0; i<ids.length; i++) {
				registPersonService.approvalRegistStudent(Long.parseLong(ids[i]), classId, approvalStudentsForm.isPass(), sessionInfo);
			}
    	}
    	return mapping.getInputForward();
	}
}