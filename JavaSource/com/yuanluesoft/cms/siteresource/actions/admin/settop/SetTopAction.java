package com.yuanluesoft.cms.siteresource.actions.admin.settop;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.siteresource.forms.SetTop;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceTop;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author chuan
 *
 */
public class SetTopAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		SetTop setTopForm = (SetTop)form;
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		//加载记录
		SiteResource siteResource = (SiteResource)siteResourceService.load(SiteResource.class, setTopForm.getResourceId());
		setTopForm.setSelectedDirectoryIds(siteResource.getResourceTops()==null ? null : new long[siteResource.getResourceTops().size()]);
		int i = 0;
		for(Iterator iterator = siteResource.getResourceTops()==null ? null : siteResource.getResourceTops().iterator(); iterator!=null && iterator.hasNext();) {
			SiteResourceTop top = (SiteResourceTop)iterator.next();
			if(i==0) {
				setTopForm.setExpire(top.getExpire()); 
			}
			setTopForm.getSelectedDirectoryIds()[i++] = top.getSiteId();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		SetTop setTopForm = (SetTop)form;
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		String[] selectedDirectoryIds = request.getParameterValues("selectedDirectoryIds");
		if(selectedDirectoryIds.length==1 && selectedDirectoryIds[0].isEmpty()) {
			setTopForm.setSelectedDirectoryIds(null);
		}
		siteResourceService.setTop(setTopForm.getResourceId(), setTopForm.getColumnIds(), setTopForm.getSelectedDirectoryIds(), setTopForm.getExpire(), sessionInfo);
	}
}