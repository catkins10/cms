package com.yuanluesoft.cms.infopublic.opinion.actions.opinion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.opinion.forms.Opinion;
import com.yuanluesoft.cms.infopublic.opinion.pojo.PublicOpinion;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PublicOpinion publicOpinion = (PublicOpinion)record;
		Opinion opinionForm = (Opinion)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			opinionForm.setSubForm("publicOpinion"); //提交页面
		}
		else if(publicOpinion!=null && publicOpinion.getPublicPass()=='1') { //已公开
			opinionForm.setSubForm("fullyOpinion");
		}
	}
}