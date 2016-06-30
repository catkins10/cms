package com.yuanluesoft.appraise.actions.participateunit.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.appraise.actions.AppraiseFormAction;
import com.yuanluesoft.appraise.forms.admin.ParticipateUnit;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ParticipateUnitAction extends AppraiseFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.actions.AppraiseFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ParticipateUnit participateUnitForm = (ParticipateUnit)form;
		participateUnitForm.setYear(DateTimeUtils.getYear(DateTimeUtils.date()));
	}
}