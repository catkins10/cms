package com.yuanluesoft.cms.advert.actions.advertput.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advert.forms.admin.AdvertPut;
import com.yuanluesoft.cms.advert.pojo.Advert;
import com.yuanluesoft.cms.advert.pojo.AdvertSpace;
import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AdvertPutAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		AdvertPut advertPutForm = (AdvertPut)form;
		com.yuanluesoft.cms.advert.pojo.AdvertPut advertPut = (com.yuanluesoft.cms.advert.pojo.AdvertPut)record;
		SiteService siteService = (SiteService)getService("siteService");
		if(!siteService.checkPopedom(advertPut==null ? advertPutForm.getSiteId() : advertPut.getSiteId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		AdvertPut advertPutForm = (AdvertPut)form;
		AdvertService advertService = (AdvertService)getService("advertService");
		Advert advert = getAdvert(advertPutForm.getAdvertId(), request);
		advertPutForm.setAdvertName(advert.getName()); //广告名称
		advertPutForm.setSpaceName(advert.getSpaceName()); //广告位名称
		advertPutForm.setCustomerName(advert.getCustomerName()); //客户名称
		advertPutForm.setBeginTime(DateTimeUtils.now()); //投放开始时间
		advertPutForm.setCreated(DateTimeUtils.now()); //登记时间
		advertPutForm.setCreator(sessionInfo.getUserName()); //登记人
		advertPutForm.setQuotedPrice(((AdvertSpace)advertService.load(AdvertSpace.class, advert.getSpaceId())).getPrice()); //报价说明
		advertPutForm.setCurrentAdvertPut(advertService.getCurrentAdvertPut(advert.getSpaceId())); //广告位当前投放的广告
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(record!=null) {
			com.yuanluesoft.cms.advert.pojo.AdvertPut advertPut = (com.yuanluesoft.cms.advert.pojo.AdvertPut)record;
			if(advertPut.getFactEndTime()!=null || (advertPut.getEndTime()!=null && !advertPut.getEndTime().after(DateTimeUtils.now()))) {
				form.getFormActions().removeFormAction("结束投放");
			}
			form.setSubForm(form.getSubForm().replace("Edit", "Read"));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.advert.pojo.AdvertPut advertPut = (com.yuanluesoft.cms.advert.pojo.AdvertPut)record;
			Advert advert = getAdvert(advertPut.getAdvertId(), request);
			advertPut.setAdvertName(advert.getName()); //广告名称
			advertPut.setSpaceName(advert.getSpaceName()); //广告位名称
			advertPut.setCustomerName(advert.getCustomerName()); //客户名称
			advertPut.setCreated(DateTimeUtils.now()); //登记时间
			advertPut.setCreator(sessionInfo.getUserName()); //登记人
			advertPut.setCreatorId(sessionInfo.getUserId()); //登记人ID
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}

	/**
	 * 获取广告
	 * @param advertId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Advert getAdvert(long advertId, HttpServletRequest request) throws ServiceException {
		Advert advert = (Advert)request.getAttribute("advert");
		if(advert==null) {
			AdvertService advertService = (AdvertService)getService("advertService");
			advert = (Advert)advertService.load(Advert.class, advertId);
		}
		return advert;
	}
}