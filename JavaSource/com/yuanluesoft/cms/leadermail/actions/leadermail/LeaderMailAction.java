package com.yuanluesoft.cms.leadermail.actions.leadermail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMail;
import com.yuanluesoft.cms.leadermail.pojo.LeaderMailType;
import com.yuanluesoft.cms.leadermail.service.LeaderMailService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class LeaderMailAction extends PublicServiceAction {
	
	public LeaderMailAction() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		}
		catch(Exception e) {
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.ApplicationAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		LeaderMail leaderMail = (LeaderMail)record;
		com.yuanluesoft.cms.leadermail.forms.LeaderMail leaderMailForm = (com.yuanluesoft.cms.leadermail.forms.LeaderMail)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			leaderMailForm.setSubForm("leaderMail"); //提交页面
		}
		else if(leaderMail!=null && Boolean.TRUE.equals(leaderMail.getExtendPropertyValue("queryResult"))) { //查询结果
			if(leaderMail.getPublicPass()!='1') { //信件未公开
				leaderMailForm.setSubForm("mailPoorQueryResult"); //信件查询结果(信件未公开)
			}
			else {
				leaderMailForm.setSubForm("mailFullQueryResult"); //信件查询结果(信件已公开)
			}
		}
		else if(leaderMail==null || leaderMail.getPublicPass()!='1') { //未公开
			leaderMailForm.setSubForm("leaderMailFailed"); //查询失败页面
		}
		else if(leaderMail.getPublicBody()=='1' && leaderMail.getPublicWorkflow()=='1') { //完全公开
			leaderMailForm.setSubForm("fullyMail");
		}
		else if(leaderMail.getPublicBody()=='1') { //公开正文
			leaderMailForm.setSubForm("originalMail");
		}
		else if(leaderMail.getPublicWorkflow()=='1') { //公开办理过程
			leaderMailForm.setSubForm("processingMail");
		}
		else { //公开主题
			leaderMailForm.setSubForm("poorMail");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#publicQueryResult(com.yuanluesoft.cms.publicservice.pojo.PublicService)
	 */
	protected void publicQueryResult(PublicService pojoPublicService) throws Exception {
		pojoPublicService.setExtendPropertyValue("queryResult", Boolean.TRUE); //标记为信件查询结果
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LeaderMail leaderMail = (LeaderMail)record;
		LeaderMailService leaderMailService = (LeaderMailService)getService("leaderMailService");
		//设置办理期限
		LeaderMailType leaderMailType = (LeaderMailType)ListUtils.findObjectByProperty(leaderMailService.listTypes(leaderMail.getSiteId()), "type", leaderMail.getType());
		leaderMail.setWorkingDay(leaderMailType==null ? 15 : leaderMailType.getWorkingDay());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}