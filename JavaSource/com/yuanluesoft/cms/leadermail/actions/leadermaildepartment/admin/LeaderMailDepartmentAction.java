package com.yuanluesoft.cms.leadermail.actions.leadermaildepartment.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.leadermail.forms.admin.LeaderMailDepartment;
import com.yuanluesoft.cms.leadermail.service.LeaderMailService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class LeaderMailDepartmentAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		LeaderMailService leaderMailService = (LeaderMailService)getService("leaderMailService");
		return leaderMailService.getLeaderMailDepartment(((LeaderMailDepartment)form).getSiteId());
	}
}