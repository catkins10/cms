/*
 * Created on 2006-6-9
 *
 */
package com.yuanluesoft.j2oa.loan.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.loan.forms.LoanForm;
import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 *
 * @author linchuan
 *
 */
public class LoanAction extends WorkflowAction {
	
    public LoanAction() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "run";
	}
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        LoanForm loanForm = (LoanForm)form;
        //设置借款人姓名
        loanForm.setLoanPersonName(sessionInfo.getUserName());
        //设置部门信息
        if(loanForm.getLoanDepartmentId()==0) {
	        loanForm.setLoanDepartmentName(sessionInfo.getDepartmentName());
	        loanForm.setLoanDepartmentId(sessionInfo.getDepartmentId());
        }
        //设置借款时间
        loanForm.setLoanDate(DateTimeUtils.date());
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Loan loan = (Loan)record;
		if(loan!=null && loan.getReimburseLoans()!=null && !loan.getReimburseLoans().isEmpty()) {
			form.getTabs().addTab(1, "reimburseLoans", "核销记录", "reimburseLoans.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Loan loan = (Loan)record;
        if(OPEN_MODE_CREATE.equals(openMode)) {
            //设置借款人信息
            loan.setLoanPersonId(sessionInfo.getUserId());
            loan.setLoanPersonName(sessionInfo.getUserName());
            //设置借款时间
            loan.setLoanDate(DateTimeUtils.date());
        }
        return super.saveRecord(form, record, openMode, request, response, sessionInfo);
    }

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#createWorklfowMessage(java.lang.Object)
     */
    protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
        WorkflowMessage workflowMessage = super.createWorklfowMessage(record, workflowForm);
        Loan loan = (Loan)record;
        workflowMessage.setContent(loan.getReason());
        return workflowMessage;
    }
}