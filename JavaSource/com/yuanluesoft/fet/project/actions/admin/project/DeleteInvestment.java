package com.yuanluesoft.fet.project.actions.admin.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.project.forms.admin.Project;
import com.yuanluesoft.fet.project.pojo.FetProject;
import com.yuanluesoft.fet.project.pojo.FetProjectInvestment;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class DeleteInvestment extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "investment", null, null);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Project projectForm = (Project)form;
	   	DatabaseService databaseService = (DatabaseService)getService("databaseService");
	   	databaseService.deleteRecordById(FetProjectInvestment.class.getName(), projectForm.getInvestmentId());
	   	projectForm.setInvestmentReceiveTime(null);
	   	projectForm.setInvestmentReceiveMoney(0);
	   	projectForm.setInvestmentId(0);
	   	//更新已到资金总额
	    String hql = "select sum(FetProjectInvestment.money), sum(FetProjectInvestment.moneyChecked) from FetProjectInvestment FetProjectInvestment where FetProjectInvestment.projectId=" + projectForm.getId();
	    FetProject projectPojo = (FetProject)record;
	    Object[] sum = (Object[])databaseService.findRecordByHql(hql);
	    projectPojo.setReceivedInvestment(sum==null ? 0 : ((Number)sum[0]).floatValue());
	    projectPojo.setReceivedChecked(sum==null ? 0 : ((Number)sum[1]).floatValue());
	    return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}