package com.yuanluesoft.cms.infopublic.request.actions.personalrequest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yuanluesoft.cms.infopublic.request.forms.PersonalRequest;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class PersonalRequestAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PublicRequest publicRequest = (PublicRequest)record;
		PersonalRequest requestForm = (PersonalRequest)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			requestForm.setSubForm("personalRequest"); //提交页面
		}
		else if(publicRequest==null || publicRequest.getPublicPass()!='1') { //未公开
			requestForm.setSubForm("requestFailed"); //查询失败页面
		}
		else if(publicRequest.getPublicBody()=='1' && publicRequest.getPublicWorkflow()=='1') { //完全公开
			requestForm.setSubForm("fullyPersonalRequest");
		}
		else if(publicRequest.getPublicBody()=='1') { //公开正文
			requestForm.setSubForm("originalPersonalRequest");
		}
		else if(publicRequest.getPublicWorkflow()=='1') { //公开办理过程
			requestForm.setSubForm("processingRequest");
		}
		else { //公开主题
			requestForm.setSubForm("poorRequest");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PersonalRequest requestForm = (PersonalRequest)form;
		requestForm.setProposerType('1'); //个人
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		PersonalRequest personalRequestForm = (PersonalRequest)form;
		if(personalRequestForm.getMedium()!=null) {
			personalRequestForm.setMediums(personalRequestForm.getMedium().split(","));
		}
		if(personalRequestForm.getReceiveMode()!=null) {
			personalRequestForm.setReceiveModes(personalRequestForm.getReceiveMode().split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generatePojo(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PublicRequest publicRequest = (PublicRequest)super.generateRecord(form, openMode, request, sessionInfo);
		PersonalRequest personalRequestForm = (PersonalRequest)form;
		publicRequest.setMedium(personalRequestForm.getMediums()==null ? null : StringUtils.join(personalRequestForm.getMediums(), ","));
		publicRequest.setReceiveMode(personalRequestForm.getReceiveModes()==null ? null : StringUtils.join(personalRequestForm.getReceiveModes(), ","));
		return publicRequest;
	}
}