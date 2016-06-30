/**
 * 
 */
package com.yuanluesoft.cms.preapproval.actions.preapproval;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.preapproval.pojo.Preapproval;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author LinChuan
 *
 */
public class PreapprovalAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Preapproval preapproval = (Preapproval)record;
		com.yuanluesoft.cms.preapproval.forms.Preapproval preapprovalForm = (com.yuanluesoft.cms.preapproval.forms.Preapproval)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			preapprovalForm.setSubForm("preapproval"); //提交页面
		}
		else if(preapproval==null || preapproval.getPublicPass()!='1') { //未公开
			preapprovalForm.setSubForm("preapprovalFailed"); //查询失败页面
		}
		else if(preapproval.getPublicBody()=='1' && preapproval.getPublicWorkflow()=='1') { //完全公开
			preapprovalForm.setSubForm("fullyPreapproval");
		}
		else if(preapproval.getPublicBody()=='1') { //公开正文
			preapprovalForm.setSubForm("originalPreapproval");
		}
		else if(preapproval.getPublicWorkflow()=='1') { //公开办理过程
			preapprovalForm.setSubForm("processingPreapproval");
		}
		else { //公开主题
			preapprovalForm.setSubForm("poorPreapproval");
		}
	}
}