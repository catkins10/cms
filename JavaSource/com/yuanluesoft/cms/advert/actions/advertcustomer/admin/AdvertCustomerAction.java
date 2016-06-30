package com.yuanluesoft.cms.advert.actions.advertcustomer.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advert.forms.admin.AdvertCustomer;
import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AdvertCustomerAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		AdvertCustomer advertCustomerForm = (AdvertCustomer)form;
		advertCustomerForm.setCreated(DateTimeUtils.now());
		advertCustomerForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		AdvertCustomer advertCustomerForm = (AdvertCustomer)form;
		advertCustomerForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			request.setAttribute("customerId", "" + advertCustomerForm.getId());
			advertCustomerForm.getTabs().addTab(-1, "adverts", "广告", "adverts.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.advert.pojo.AdvertCustomer advertCustomer = (com.yuanluesoft.cms.advert.pojo.AdvertCustomer)record;
			if(advertCustomer.getCreated()==null) {
				advertCustomer.setCreated(DateTimeUtils.now());
			}
			advertCustomer.setCreator(sessionInfo.getUserName());
			advertCustomer.setCreatorId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}