package com.yuanluesoft.jeaf.usermanage.actions.admin.schoolclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.actions.admin.org.OrgAction;
import com.yuanluesoft.jeaf.usermanage.forms.admin.SchoolClass;
import com.yuanluesoft.jeaf.usermanage.pojo.School;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolClassAction extends OrgAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.org.OrgAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		OrgService orgService = (OrgService)getService("orgService");
		SchoolClass schoolClassForm = (SchoolClass)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			//设置年级列表
			School school = (School)getOrgService().getOrg(schoolClassForm.getParentDirectoryId());
			List grades;
			if("完全中学".equals(school.getCategory())) {
				grades = orgService.listGradesByStage("初中");
				grades.addAll(orgService.listGradesByStage("高中"));
			}
			else {
				grades = orgService.listGradesByStage(school.getCategory());
			}
			schoolClassForm.setGrades(grades);
		}
		List curriculums = orgService.listCurriculums(false);
		List teacherTitles = new ArrayList();
		teacherTitles.add("班主任");
		for(Iterator iterator = curriculums.iterator(); iterator.hasNext();) {
			teacherTitles.add((String)iterator.next() + "老师");
		}
		schoolClassForm.setTeacherTitles(teacherTitles);
		//设置URL
		String webApplicationSafeUrl = (String)getBean("webApplicationSafeUrl");
		schoolClassForm.setWebApplicationSafeUrl(webApplicationSafeUrl);
		//设置TAB列表
		schoolClassForm.getTabs().addTab(-1, "basic", "基本信息", null, true);
		schoolClassForm.getTabs().addTab(-1, "teacher", "班主任/任课老师", null, false);
	}
}
