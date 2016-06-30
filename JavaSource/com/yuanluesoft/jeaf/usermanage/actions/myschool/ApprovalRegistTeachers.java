package com.yuanluesoft.jeaf.usermanage.actions.myschool;

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
public class ApprovalRegistTeachers extends ListToApprovalTeachers {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.jeaf.usermanage.forms.ListToApprovalTeachers approvalTeachersForm = (com.yuanluesoft.jeaf.usermanage.forms.ListToApprovalTeachers)viewForm;
    	//审批
    	String registTeacherIds = approvalTeachersForm.getViewPackage().getSelectedIds();
    	if(registTeacherIds!=null && !registTeacherIds.equals("")) {
        	RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
        	//获取用户任管理员的学校
    		List schools = getOrgService().listManagedSchools(sessionInfo.getUserId());
    		if(schools==null || schools.isEmpty()) {
    			throw new PrivilegeException();
    		}
    		long schoolId = ((Org)schools.get(0)).getId();
			String[] ids = registTeacherIds.split(",");
			for(int i=0; i<ids.length; i++) {
				registPersonService.approvalRegistTeacher(Long.parseLong(ids[i]), schoolId, approvalTeachersForm.isPass(), sessionInfo);
			}
    	}
    	return mapping.getInputForward();
	}
}