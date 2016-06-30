package com.yuanluesoft.cms.advert.actions.advertspace.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advert.forms.admin.AdvertSpace;
import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AdvertSpaceAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		AdvertSpace advertSpaceForm = (AdvertSpace)form;
		advertSpaceForm.setCreated(DateTimeUtils.now());
		advertSpaceForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		AdvertSpace advertSpaceForm = (AdvertSpace)form;
		advertSpaceForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.advert.pojo.AdvertSpace advertSpace = (com.yuanluesoft.cms.advert.pojo.AdvertSpace)record;
			if(advertSpace.getIsFloat()==1) { //浮动广告
				advertSpaceForm.getTabs().addTab(-1, "putPages", "投放页面", "advertPutPages.jsp", false);
			}
			request.setAttribute("spaceId", "" + advertSpaceForm.getId());
			advertSpaceForm.getTabs().addTab(-1, "adverts", "广告", "adverts.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.advert.pojo.AdvertSpace advertSpace = (com.yuanluesoft.cms.advert.pojo.AdvertSpace)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(advertSpace.getCreated()==null) {
				advertSpace.setCreated(DateTimeUtils.now());
			}
			advertSpace.setCreator(sessionInfo.getUserName());
			advertSpace.setCreatorId(sessionInfo.getUserId());
		}
		advertSpace.setFreeContent(StringUtils.removeServerPath(advertSpace.getFreeContent(), request));
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}