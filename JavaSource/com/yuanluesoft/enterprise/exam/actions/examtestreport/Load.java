package com.yuanluesoft.enterprise.exam.actions.examtestreport;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.exam.forms.admin.ExamTestReport;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaper;
import com.yuanluesoft.enterprise.exam.pojo.ExamTest;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends DialogFormAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_create")) {
			return;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		ExamTestReport examTestReport = (ExamTestReport)form;
		ExamService examService = (ExamService)getService("examService");
		//获取需要参加考试的人员
		List notTestPersons = getRecordControlService().listVisitorPersons(examTestReport.getExamPaperId(), ExamPaper.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY);
		examTestReport.setNotTestPersons(notTestPersons);
		//获取答卷
		List examTests = examService.listExamTests(examTestReport.getExamPaperId());
		examTestReport.setTested(examTests);
		//从需要参加考试的人员中剔除已经考过的用户
		for(Iterator iterator = examTests==null ? null : examTests.iterator(); iterator!=null && iterator.hasNext();) {
			ExamTest examTest = (ExamTest)iterator.next();
			ListUtils.removeObjectByProperty(notTestPersons, "id", new Long(examTest.getPersonId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }
}