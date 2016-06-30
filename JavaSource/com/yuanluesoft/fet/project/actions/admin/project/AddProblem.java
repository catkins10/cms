package com.yuanluesoft.fet.project.actions.admin.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.project.forms.admin.Project;
import com.yuanluesoft.fet.project.pojo.FetProjectProblem;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class AddProblem extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "problemtab", null, null);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Project projectForm = (Project)form;
        if(projectForm.getProblem()!=null && !projectForm.getProblem().equals("")) {
	        DatabaseService databaseService = (DatabaseService)getService("databaseService");
	        //保存问题描述
	        FetProjectProblem problem = new FetProjectProblem();
	        problem.setProblem(projectForm.getProblem()); //内容
	        problem.setProjectId(projectForm.getId());
	        if(projectForm.getProblemId()==0) { //新纪录
	        	problem.setCreated(DateTimeUtils.now()); //时间
		        problem.setId(UUIDLongGenerator.generateId());
	        	databaseService.saveRecord(problem);
	        }
	        else { //更新旧记录
	        	problem.setId(projectForm.getProblemId());
	        	problem.setCreated(projectForm.getProblemCreated()); //时间
		        databaseService.updateRecord(problem);
	        }
	        projectForm.setProblemId(0);
	        projectForm.setProblem(null);
        }
        return record;
	}
}