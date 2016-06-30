/**
 * 
 */
package com.yuanluesoft.jeaf.usermanage.actions.myschool.schoolclass;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.usermanage.forms.SchoolClass;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 *
 * @author LinChuan
 *
 */
public class SchoolClassAction extends com.yuanluesoft.jeaf.usermanage.actions.admin.schoolclass.SchoolClassAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SchoolClass schoolClassForm = (SchoolClass)form;
		if(OPEN_MODE_CREATE.equals(schoolClassForm.getAct())) {
			//获取用户任管理员的学校
			SessionInfo sessionInfo = getSessionInfo(request, response);
			List schools = getOrgService().listManagedSchools(sessionInfo.getUserId());
			if(schools==null || schools.isEmpty()) {
				throw new PrivilegeException();
			}
			Org school = (Org)schools.get(0);
			
			schoolClassForm.setParentDirectoryId(school.getId());
		}
		return super.load(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.org.OrgAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		SchoolClass schoolClassForm = (SchoolClass)form;
		//获取班级的学生列表
		schoolClassForm.setStudents(getOrgService().listClassStudents(schoolClassForm.getId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		form.getTabs().addTab(-1, "teacher", "班主任/任课老师", null, false);
		form.getTabs().addTab(-1, "student", "学生", null, false);
	}
}
