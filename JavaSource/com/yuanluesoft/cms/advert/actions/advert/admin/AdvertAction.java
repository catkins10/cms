package com.yuanluesoft.cms.advert.actions.advert.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import com.yuanluesoft.cms.advert.forms.admin.Advert;
import com.yuanluesoft.cms.advert.pojo.AdvertCustomer;
import com.yuanluesoft.cms.advert.pojo.AdvertSpace;
import com.yuanluesoft.cms.advert.service.AdvertService;
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
public class AdvertAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Advert advertForm = (Advert)form;
		AdvertService advertService = (AdvertService)getService("advertService");
		advertForm.setCreated(DateTimeUtils.now());
		advertForm.setCreator(sessionInfo.getUserName());
		//获取广告位信息
		if(advertForm.getSpaceId()>0) {
			AdvertSpace advertSpace = (AdvertSpace)advertService.load(AdvertSpace.class, advertForm.getSpaceId());
			advertForm.setSpaceName(advertSpace.getName());
			advertForm.setWidth(advertSpace.getWidth());
			advertForm.setHeight(advertSpace.getHeight());
		}
		//获取客户信息
		if(advertForm.getCustomerId()>0) {
			AdvertCustomer advertCustomer = (AdvertCustomer)advertService.load(AdvertCustomer.class, advertForm.getCustomerId());
			advertForm.setCustomerName(advertCustomer.getName());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.advert.pojo.Advert advert = (com.yuanluesoft.cms.advert.pojo.Advert)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(advert.getCreated()==null) {
				advert.setCreated(DateTimeUtils.now());
			}
			advert.setCreator(sessionInfo.getUserName());
			advert.setCreatorId(sessionInfo.getUserId());
		}
		advert.setContent(StringUtils.removeServerPath(advert.getContent(), request));
		advert.setMinimizeContent(StringUtils.removeServerPath(advert.getMinimizeContent(), request));
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/**
	 * 更新父页面
	 * @param forward
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward refreshOpener(ActionForward forward, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(forward!=null && "result".equals(forward.getName())) {
    		Advert advertForm = (Advert)form;
    		if("space".equals(advertForm.getOpener())) {
	    		refreshMainRecord(advertForm, "refreshAdvertSpace", "adverts", request, response);
	    		forward = null;
    		}
    		else if("customer".equals(advertForm.getOpener())) {
    			refreshMainRecord(advertForm, "refreshAdvertCustomer", "adverts", request, response);
	    		forward = null;
    		}
    	}
    	return forward;
    }
}