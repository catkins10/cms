package com.yuanluesoft.cms.complaint.actions.complaint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.complaint.pojo.Complaint;
import com.yuanluesoft.cms.complaint.pojo.ComplaintType;
import com.yuanluesoft.cms.complaint.service.ComplaintService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintAction extends PublicServiceAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.ApplicationAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Complaint complaint = (Complaint)record;
		com.yuanluesoft.cms.complaint.forms.Complaint complaintForm = (com.yuanluesoft.cms.complaint.forms.Complaint)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			complaintForm.setSubForm("complaint"); //提交页面
		}
		else if(complaint==null || complaint.getPublicPass()!='1') { //未公开
			complaintForm.setSubForm("complaintFailed"); //查询失败页面
		}
		else if(complaint.getPublicBody()=='1' && complaint.getPublicWorkflow()=='1') { //完全公开
			complaintForm.setSubForm("fullyComplaint");
		}
		else if(complaint.getPublicBody()=='1') { //公开正文
			complaintForm.setSubForm("originalComplaint");
		}
		else if(complaint.getPublicWorkflow()=='1') { //公开办理过程
			complaintForm.setSubForm("processingComplaint");
		}
		else { //公开主题
			complaintForm.setSubForm("poorComplaint");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Complaint complaint = (Complaint)record;
		ComplaintService complaintService = (ComplaintService)getService("complaintService");
		//设置办理期限
		ComplaintType complaintType = (ComplaintType)ListUtils.findObjectByProperty(complaintService.listTypes(complaint.getSiteId()), "type", complaint.getType());
		complaint.setWorkingDay(complaintType==null ? 15 : complaintType.getWorkingDay());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}