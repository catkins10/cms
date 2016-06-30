package com.yuanluesoft.cms.infopublic.request.actions.requestnotify.admin;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.infopublic.request.actions.request.admin.RequestAction;
import com.yuanluesoft.cms.infopublic.request.forms.admin.RequestNotify;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequestNotify;
import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class RequestNotifyAction extends RequestAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		PublicRequest publicRequest = (PublicRequest)mainRecord;
		return publicRequest==null || publicRequest.getNotify()==null || publicRequest.getNotify().isEmpty() ? null : (PublicRequestNotify)publicRequest.getNotify().iterator().next();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		PublicRequest publicRequest = (PublicRequest)mainRecord;
		PublicRequestNotify requestNotify = new PublicRequestNotify();
		requestNotify.setPublicRequest(publicRequest); //信息公开申请
		initRequestNotify(requestNotify, sessionInfo);
		//设置通知书页面
		TemplateService templateService = (TemplateService)getService("templateService");
		HTMLDocument htmlDocument = templateService.getTemplateHTMLDocument("cms/infopublic/request", "requestNotify", publicRequest.getSiteId(), 0, TemplateThemeService.THEME_TYPE_COMPUTER, 0, true, false, request);
		if(htmlDocument!=null) {
			PageBuilder pageBuilder = (PageBuilder)getService("pageBuilder");
			HTMLParser htmlParser = (HTMLParser)getService("htmlParser");
			SitePage sitePage = new SitePage();
			sitePage.setAttribute("record", requestNotify);
			htmlDocument = pageBuilder.buildHTMLDocument(htmlDocument, publicRequest.getSiteId(), sitePage, request, false, false, false, false);
			RequestNotify notifyForm = (RequestNotify)form;
			notifyForm.getRequestNotify().setNotify(htmlParser.getBodyHTML(htmlDocument, "utf-8", false));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			PublicRequestNotify requestNotify = (PublicRequestNotify)component;
			initRequestNotify(requestNotify, sessionInfo);
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}

	/**
	 * 初始化告知书
	 * @param requestNotify
	 * @param sessionInfo
	 * @throws Exception
	 */
	private void initRequestNotify(PublicRequestNotify requestNotify, SessionInfo sessionInfo) throws Exception {
		requestNotify.setCreated(DateTimeUtils.now()); //创建时间
		requestNotify.setCreatorId(sessionInfo.getUserId()); //告知人ID
		requestNotify.setCreator(sessionInfo.getUserName()); //告知人
		requestNotify.setCreatorUnit(sessionInfo.getUnitName()); //告知人所在单位
		Org org = getOrgService().getOrg(sessionInfo.getUnitId());
		if(org instanceof Unit) {
			Unit unit = (Unit)org;
			requestNotify.setCreatorUnit(unit.getFullName()==null || unit.getFullName().isEmpty() ? sessionInfo.getUnitName() : unit.getFullName());
		}
	}
}