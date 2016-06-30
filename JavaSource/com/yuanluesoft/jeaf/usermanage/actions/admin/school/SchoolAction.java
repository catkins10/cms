package com.yuanluesoft.jeaf.usermanage.actions.admin.school;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.actions.admin.org.OrgAction;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolDepartment;
import com.yuanluesoft.jeaf.usermanage.pojo.School;
import com.yuanluesoft.jeaf.usermanage.pojo.Teacher;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolAction extends OrgAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置学校部门列表
		OrgService orgService = (OrgService)getService("orgService");
		List curriculums = orgService.listCurriculums(false);
		List departmentNames = new ArrayList();
		for(Iterator iterator = curriculums.iterator(); iterator.hasNext();) {
			String curriculum = (String)iterator.next();
			departmentNames.add(curriculum + "教研组");
		}
		com.yuanluesoft.jeaf.usermanage.forms.admin.School schoolForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.School)form;
		schoolForm.setDepartmentNames(departmentNames);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.org.OrgAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.jeaf.usermanage.forms.admin.School schoolForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.School)form;
		schoolForm.setManagerSex('M');
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.org.OrgAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) { //新注册
			School school = (School)record;
			com.yuanluesoft.jeaf.usermanage.forms.admin.School schoolForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.School)form;
			OrgService orgService = getOrgService();
			//注册部门/教研组
			long managerDepartmentId = 0;
			String[] departmentNames = schoolForm.getRegistDepartmentNames();
			int departmentIndex = 0;
			for(; departmentNames!=null && departmentIndex<departmentNames.length; departmentIndex++) {
				SchoolDepartment department = orgService.addSchoolDepartment(departmentNames[departmentIndex], school.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
				if(departmentNames[departmentIndex].equals(schoolForm.getManagerDepartment())) { //设置管理员部门ID
					managerDepartmentId = department.getId();
				}
			}
			//注册班级
			String[] gradeNames = schoolForm.getRegistGradeNames();
			String[] gradeClassCounts = schoolForm.getRegistGradeClassCounts();
			String[] gradeEnrollYears = schoolForm.getRegistGradeEnrollYears();
			String[] lengthOfSchoolings = schoolForm.getRegistLengthOfSchoolings();
			for(int i=0; gradeNames!=null && i<gradeNames.length; i++) {
				int classCount = 0;
				try {
					classCount =  Integer.parseInt(gradeClassCounts[i]);
					for(int j=1; j<=classCount; j++) {
						orgService.addClass(gradeNames[i].replaceFirst("级", "") + "（" + j + "）班", j, Integer.parseInt(gradeEnrollYears[i]), Integer.parseInt(lengthOfSchoolings[i]), school.getId(), sessionInfo.getUserId(), sessionInfo.getUserName(), null);
					}
				}
				catch (NumberFormatException e) {
					continue;
				}
			}
			//注册管理员
			if(schoolForm.getManagerName()!=null && !schoolForm.getManagerName().equals("")) {
				if(schoolForm.getManagerDepartment()==null || schoolForm.getManagerDepartment().equals("")) { //不指定部门,注册到学校里
					managerDepartmentId = school.getId();
				}
				else if(managerDepartmentId==0) { //管理员所在部门未创建
					//创建管理员所在部门
					SchoolDepartment department = orgService.addSchoolDepartment(schoolForm.getManagerDepartment(), school.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
					managerDepartmentId = department.getId();
				}
				RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
				PersonService personService = (PersonService)getService("personService");
				//注册管理员
				String password = UUIDStringGenerator.generateId().substring(0, 6);
				Teacher manager = personService.addTeacher(schoolForm.getManagerName(), null, password, schoolForm.getManagerSex(), schoolForm.getManagerTel(), null, schoolForm.getManagerMobile(), null, schoolForm.getManagerMail(), "" + managerDepartmentId, sessionInfo.getUserId(), sessionInfo.getUserName());
				//授予管理员权限
			    orgService.authorize(school, true, "manager", manager.getId() + "", manager.getName(), false, sessionInfo);
		        //生成学校的注册码
			    String schoolRegistCode = UUIDStringGenerator.generateId().substring(0, 6);
		        registPersonService.createSchoolRegistCode(school.getId(), schoolRegistCode);
		        //发送邮件给管理员
		        if(schoolForm.getManagerMail()!=null && !schoolForm.getManagerMail().equals("")) {
		        	try {
		        		registPersonService.sendSchoolRegistMail(schoolForm.getManagerMail(), school.getDirectoryName(), manager.getName(), password, manager.getLoginName(), schoolRegistCode);
		        	}
		        	catch(Exception e) {
		        		
		        	}
		        }
		        schoolForm.setActionResult("注册成功！（管理员帐号：" + manager.getLoginName() + "，密码：" + password + "，学校验证码：" + schoolRegistCode + "）");
			}
		}
		return record;
	}
}