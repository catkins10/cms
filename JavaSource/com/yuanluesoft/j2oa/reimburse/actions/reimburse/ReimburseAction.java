/*
 * Created on 2006-6-16
 *
 */
package com.yuanluesoft.j2oa.reimburse.actions.reimburse;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.j2oa.loan.service.LoanService;
import com.yuanluesoft.j2oa.reimburse.forms.ReimburseForm;
import com.yuanluesoft.j2oa.reimburse.pojo.Reimburse;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseCharge;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseLoan;
import com.yuanluesoft.j2oa.reimburse.service.ReimburseService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 *
 * @author linchuan
 *
 */
public class ReimburseAction extends WorkflowAction {
  
	public ReimburseAction() {
		super();
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "run";
	}

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, boolean)
     */
    public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        Reimburse reimburse = (Reimburse)record;
        ReimburseForm reimburseForm = (ReimburseForm)form;
      
		//设置核销金额列表
		if(reimburse.getReimburseLoans()!=null) {
		    String money = null;
		    for(Iterator iterator = reimburse.getReimburseLoans().iterator(); iterator.hasNext();) {
		        ReimburseLoan reimburseLoan = (ReimburseLoan)iterator.next();
		        money = (money==null ? "" : money + ",") + reimburseLoan.getLoanId() + "," + reimburseLoan.getReimburseMoney();
		    }
		    reimburseForm.setReimburseLoanMoney(money);
		}
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        ReimburseForm reimburseForm = (ReimburseForm)form;
        //设置报销人姓名
        reimburseForm.setPersonName(sessionInfo.getUserName());
        //设置部门信息
        if(reimburseForm.getDepartmentId()==0) {
	        reimburseForm.setDepartmentName(sessionInfo.getDepartmentName());
	        reimburseForm.setDepartmentId(sessionInfo.getDepartmentId());
        }
        //设置报销时间
        reimburseForm.setReimburseDate(DateTimeUtils.date());
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		ReimburseForm reimburseForm = (ReimburseForm)form;
		Reimburse reimburse = (Reimburse)record;
        //生成待核销借款单列表
		if(accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) {
		    resetReimburseLoans(reimburseForm, reimburse, sessionInfo);
		}
		form.getTabs().addTab(1, "charges", "费用清单", "charges.jsp", false);
		if(reimburseForm.getReimburseLoans()!=null && !reimburseForm.getReimburseLoans().isEmpty()) {
			form.getTabs().addTab(2, "reimburseLoans", "借款核销", "reimburseLoan" + (reimburseForm.getSubForm().indexOf("Read")!=-1 ? "Read" : "Edit") + ".jsp", false);
		}
	}

	/**
     * 生成待核销借款单列表,包括需要核销的核不需要核销的
     * @param reimburseForm
     * @param sessionInfo
     * @throws Exception
     */
    private void resetReimburseLoans(ReimburseForm reimburseForm, Reimburse reimburse, SessionInfo sessionInfo) throws Exception {
        //获取当前用户尚需还款的借款单
        List loans = ((LoanService)getService("loanService")).listPersonalTorepayLoans(sessionInfo);
        if(loans==null || loans.isEmpty()) {
            reimburseForm.setReimburseLoans(null);
            return;
        }
        Set reimburseLoans = new LinkedHashSet();
        for(Iterator iterator = loans.iterator(); iterator.hasNext();) {
            Loan loan = (Loan)iterator.next();
            ReimburseLoan reimburseLoan = new ReimburseLoan();
            reimburseLoan.setId(UUIDLongGenerator.generateId());
            reimburseLoan.setLoan(loan);
            reimburseLoan.setLoanId(loan.getId());
            reimburseLoan.setReimburseId(reimburseForm.getId());
            //查找待核销借款单
            ReimburseLoan pojoReimburseLoan = reimburse==null ? null : (ReimburseLoan)ListUtils.findObjectByProperty(reimburse.getReimburseLoans(), "loanId", new Long(loan.getId()));
            if(pojoReimburseLoan!=null) {
                reimburseLoan.setReimburseMoney(pojoReimburseLoan.getReimburseMoney());
            }
            reimburseLoans.add(reimburseLoan);
        }
        reimburseForm.setReimburseLoans(reimburseLoans);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    Reimburse reimburse = (Reimburse)record;
        if(OPEN_MODE_CREATE.equals(openMode)) {
            //设置报销人信息
	        reimburse.setPersonId(sessionInfo.getUserId());
	        reimburse.setPersonName(sessionInfo.getUserName());
	        //设置报销时间
	        reimburse.setReimburseDate(DateTimeUtils.date());
        }
        super.saveRecord(form, record, openMode, request, response, sessionInfo);
        //保存核销记录
        ReimburseForm reimburseForm = (ReimburseForm)form;
        ((ReimburseService)getService("reimburseService")).saveReimburseLoans(reimburse, reimburseForm.getReimburseLoanMoney());
        return record;
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deletePojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Reimburse reimburse = (Reimburse)record;
        //删除票据
        if(reimburse.getCharges()!=null && !reimburse.getCharges().isEmpty()) {
            AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
            for(Iterator iterator = reimburse.getCharges().iterator(); iterator.hasNext();) {
            	ReimburseCharge charge = (ReimburseCharge)iterator.next();
                attachmentService.deleteAll(form.getFormDefine().getApplicationName(), "charge", charge.getId());
            }
        }
        super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
    }

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#createWorklfowMessage(java.lang.Object)
     */
    protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
        WorkflowMessage workflowMessage = super.createWorklfowMessage(record, workflowForm);
        Reimburse reimburse = (Reimburse)record;
        workflowMessage.setContent(reimburse.getDescription() + "(" + reimburse.getPersonName() + ")");
        return workflowMessage;
    }
}