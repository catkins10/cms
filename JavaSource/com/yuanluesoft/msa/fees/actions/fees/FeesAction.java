package com.yuanluesoft.msa.fees.actions.fees;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.msa.fees.forms.Fees;
import com.yuanluesoft.msa.fees.pojo.MsaFees;
import com.yuanluesoft.msa.fees.pojo.MsaFeesItem;

/**
 * 
 * @author linchuan
 *
 */
public class FeesAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runFees";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Fees feesForm = (Fees)form;
		feesForm.setCreated(DateTimeUtils.now());
		feesForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		//如果是分支局用户,只显示自己的细项
		Fees feesForm = (Fees)form;
		List myItems = ListUtils.getSubListByProperty(feesForm.getItems(), "unitId", new Long(sessionInfo.getUnitId()));
		if(myItems!=null && !myItems.isEmpty()) {
			feesForm.setItems(new LinkedHashSet(myItems));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//如果处于子表单处于"办理"状态,检查用户是否已经完成全部细项的征缴,如果没有,屏蔽“办理完毕”或“发送”操作
		if(form.getSubForm().endsWith("Transact.jsp")) {
			boolean completed = true;
			MsaFees fees = (MsaFees)record;
			for(Iterator iterator = fees.getItems().iterator(); iterator.hasNext();) {
				MsaFeesItem item = (MsaFeesItem)iterator.next();
				if(item.getUnitId()==sessionInfo.getUnitId() && item.getCompleteTime()==null) {
					completed = false;
					break;
				}
			}
			if(!completed) {
				form.getFormActions().removeFormAction("办理完毕");
				form.getFormActions().removeFormAction("发送");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			MsaFees fees = (MsaFees)record;
			fees.setCreated(DateTimeUtils.now());
			fees.setCreator(sessionInfo.getUserName());
			fees.setCreatorId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}