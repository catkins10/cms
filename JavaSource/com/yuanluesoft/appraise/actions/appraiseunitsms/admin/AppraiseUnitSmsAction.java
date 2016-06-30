package com.yuanluesoft.appraise.actions.appraiseunitsms.admin;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.appraise.actions.AppraiseFormAction;
import com.yuanluesoft.appraise.model.ParticipateUnit;
import com.yuanluesoft.appraise.pojo.AppraiseUnitSms;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseUnitSmsAction extends AppraiseFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(record!=null && ((AppraiseUnitSms)record).getSendTime()!=null) {
			form.setSubForm("Read");
		}
		if(accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) {
			//设置参评单位列表
			com.yuanluesoft.appraise.forms.admin.AppraiseUnitSms appraiseUnitSmsForm = (com.yuanluesoft.appraise.forms.admin.AppraiseUnitSms)form;
			AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
			List units = appraiseService.listParticipateUnits(getManagedAreaId(request, sessionInfo).longValue(), DateTimeUtils.getYear(DateTimeUtils.date()));
			request.setAttribute("participateUnits", units);
			String participateUnits = null;
			for(Iterator iterator = units==null ? null : units.iterator(); iterator!=null && iterator.hasNext();) {
				ParticipateUnit participateUnit = (ParticipateUnit)iterator.next();
				participateUnits = (participateUnits==null ? "" : participateUnits + ",") + participateUnit.getUnitName() + "|" + participateUnit.getUnitId();
			}
			appraiseUnitSmsForm.setParticipateUnits(participateUnits);
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.actions.AppraiseFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		List units = (List)request.getAttribute("participateUnits");
		com.yuanluesoft.appraise.forms.admin.AppraiseUnitSms appraiseUnitSmsForm = (com.yuanluesoft.appraise.forms.admin.AppraiseUnitSms)form;
		appraiseUnitSmsForm.setUnitIds(ListUtils.join(units, "unitId", ",", false));
		appraiseUnitSmsForm.setUnitNames(ListUtils.join(units, "unitName", ",", false));
	}
}