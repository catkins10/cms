package com.yuanluesoft.cms.infopublic.request.actions.companyrequest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.infopublic.request.actions.personalrequest.PersonalRequestAction;
import com.yuanluesoft.cms.infopublic.request.forms.CompanyRequest;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class CompanyRequestAction extends PersonalRequestAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		CompanyRequest requestForm = (CompanyRequest)form;
		requestForm.setProposerType('2'); //公司
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.request.actions.personalrequest.PersonalRequestAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PublicRequest publicRequest = (PublicRequest)record;
		CompanyRequest requestForm = (CompanyRequest)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			requestForm.setSubForm("companyRequest"); //提交页面
		}
		else if(publicRequest==null || publicRequest.getPublicPass()!='1') { //未公开
			requestForm.setSubForm("requestFailed"); //查询失败页面
		}
		else if(publicRequest.getPublicBody()=='1' && publicRequest.getPublicWorkflow()=='1') { //完全公开
			requestForm.setSubForm("fullyCompanyRequest");
		}
		else if(publicRequest.getPublicBody()=='1') { //公开正文
			requestForm.setSubForm("originalCompanyRequest");
		}
		else if(publicRequest.getPublicWorkflow()=='1') { //公开办理过程
			requestForm.setSubForm("processingRequest");
		}
		else { //公开主题
			requestForm.setSubForm("poorRequest");
		}
	}
}