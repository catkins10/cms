package com.yuanluesoft.appraise.appraiser.actions.importappraiser.admin;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.appraise.appraiser.forms.admin.ImportAppraiser;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
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
public class ImportAppraiserAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ImportAppraiser importAppraiserForm = (ImportAppraiser)form;
		if(getOrgService().checkPopedom(importAppraiserForm.getOrgId(), "appraiseManager", sessionInfo)) { //检查用户的评议管理权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ImportAppraiser importAppraiserForm = (ImportAppraiser)form;
		importAppraiserForm.setOrgName(getOrgService().getDirectoryName(importAppraiserForm.getOrgId())); //组织机构名称
		importAppraiserForm.setExpire(DateTimeUtils.add(DateTimeUtils.getYearEnd(), Calendar.YEAR, 4) ); //有效期,5年
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ImportAppraiser importAppraiserForm = (ImportAppraiser)form;
		AppraiserService appraiserService = (AppraiserService)getService("appraiserService");
		try {
			appraiserService.importAppraisers(importAppraiserForm.getId(), importAppraiserForm.getOrgId(), importAppraiserForm.getExpire(), importAppraiserForm.getAppraiserType(), AppraiserService.APPRAISER_STATUS_ENABLED, sessionInfo);
		}
		catch(ServiceException se) {
			if(se.getMessage()!=null) {
				importAppraiserForm.setError(se.getMessage());
				throw new ValidateException();
			}
			throw se;
		}
		return null;
	}
}