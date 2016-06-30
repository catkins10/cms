package com.yuanluesoft.jeaf.usermanage.actions.myschool.schoolgrade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.usermanage.forms.SchoolGrade;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolGradeAction extends com.yuanluesoft.jeaf.usermanage.actions.admin.schoolgrade.SchoolGradeAction {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SchoolGrade schoolGradeForm = (SchoolGrade)form;
		//获取用户任管理员的学校
		SessionInfo sessionInfo = getSessionInfo(request, response);
		List schools = getOrgService().listManagedSchools(sessionInfo.getUserId());
		if(schools==null || schools.isEmpty()) {
			throw new PrivilegeException();
		}
		Org school = (Org)schools.get(0);
		schoolGradeForm.setParentOrgId(school.getId());
		return super.load(form, request, response);
	}
}
