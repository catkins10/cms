package com.yuanluesoft.job.talent.actions.talent.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.job.talent.pojo.JobTalent;

/**
 * 
 * @author linchuan
 *
 */
public class TalentAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.actions.company.CompanyAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_approval") || acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains(AccessControlService.ACL_APPLICATION_VISITOR)) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		JobTalent talent = (JobTalent)record;
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		//if(company!=null && company.getJobs()!=null && !company.getJobs().isEmpty()) {
		//	form.getTabs().addTab(-1, "basic", "招聘岗位", "companyJobs.jsp", false);
		//}
		form.getTabs().addTab(-1, "employments", "就业跟踪调查表", "employments.jsp", false);
		form.getTabs().addTab(-1, "schoolings", "教育经历", "schoolings.jsp", false);
		form.getTabs().addTab(-1, "intentions", "求职意向", "intentions.jsp", false);
		form.getTabs().addTab(-1, "trainings", "培训经历", "trainings.jsp", false);
		form.getTabs().addTab(-1, "speechs", "语言能力", "speechs.jsp", false);
		form.getTabs().addTab(-1, "careers", "工作经历", "careers.jsp", false);
		form.getTabs().addTab(-1, "projects", "项目经验", "projects.jsp", false);
		form.getTabs().addTab(-1, "certificates", "证书", "certificates.jsp", false);
		form.getTabs().addTab(-1, "abilities", "其它技能", "abilities.jsp", false);
		if(talent!=null && talent.getStatus()!=0) {
			form.getFormActions().removeFormAction("审核");
		}
	}
}