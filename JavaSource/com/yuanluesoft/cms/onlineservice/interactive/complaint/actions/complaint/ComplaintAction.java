package com.yuanluesoft.cms.onlineservice.interactive.complaint.actions.complaint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAction;
import com.yuanluesoft.cms.onlineservice.interactive.complaint.forms.Complaint;
import com.yuanluesoft.cms.onlineservice.interactive.complaint.pojo.OnlineServiceComplaint;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintAction extends OnlineServiceInteractiveAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		OnlineServiceComplaint complaint = (OnlineServiceComplaint)record;
		Complaint complaintForm = (Complaint)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			complaintForm.setSubForm("onlineServiceComplaint"); //提交页面
		}
		else if(complaint==null || complaint.getPublicPass()!='1') { //未公开
			complaintForm.setSubForm("onlineServiceComplaintFailed"); //查询失败页面
		}
		else if(complaint.getPublicBody()=='1' && complaint.getPublicWorkflow()=='1') { //完全公开
			complaintForm.setSubForm("fullyOnlineServiceComplaint");
		}
		else if(complaint.getPublicBody()=='1') { //公开正文
			complaintForm.setSubForm("originalOnlineServiceComplaint");
		}
		else if(complaint.getPublicWorkflow()=='1') { //公开办理过程
			complaintForm.setSubForm("processingOnlineServiceComplaint");
		}
		else { //公开主题
			complaintForm.setSubForm("poorOnlineServiceComplaint");
		}
	}
}