package com.yuanluesoft.cms.situation.actions.situation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.situation.pojo.Situation;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SituationAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Situation situation = (Situation)record;
		com.yuanluesoft.cms.situation.forms.Situation situationForm = (com.yuanluesoft.cms.situation.forms.Situation)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			situationForm.setSubForm("situation"); //提交页面
		}
		else if(situation==null || situation.getPublicPass()!='1') { //未公开
			situationForm.setSubForm("situationFailed"); //查询失败页面
		}
		else if(situation.getPublicBody()=='1' && situation.getPublicWorkflow()=='1') { //完全公开
			situationForm.setSubForm("fullySituation");
		}
		else if(situation.getPublicBody()=='1') { //公开正文
			situationForm.setSubForm("originalSituation");
		}
		else if(situation.getPublicWorkflow()=='1') { //公开办理过程
			situationForm.setSubForm("processingSituation");
		}
		else { //公开主题
			situationForm.setSubForm("poorSituation");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.cms.situation.forms.Situation situationForm = (com.yuanluesoft.cms.situation.forms.Situation)form;
		SiteService siteService = (SiteService)getService("siteService");
		WebSite site = siteService.getParentSite(RequestUtils.getParameterLongValue(request, "siteId"));
		situationForm.setUnitId(site==null ? 0 : site.getOwnerUnitId());
		//检查是否有下级单位,如果没有则设置单位名称
		if(situationForm.getUnitId()>0) {
			List childUnits = getOrgService().listChildDirectories(situationForm.getUnitId(), "unit", null, null, false, false, sessionInfo, 0, 1);
			if(childUnits==null || childUnits.isEmpty()) {
				situationForm.setUnitName(site.getOwnerUnitName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			Situation situation = (Situation)record;
			situation.setReceiveTime(DateTimeUtils.now());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}