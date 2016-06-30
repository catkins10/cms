package com.yuanluesoft.logistics.usermanage.actions.blacklist.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsBlacklist;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;

/**
 * 
 * @author linchuan
 *
 */
public class Remove extends BlacklistAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.usermanage.actions.blacklist.admin.BlacklistAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsBlacklist blacklist = (LogisticsBlacklist)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
		logisticsUserService.removeFromBlacklist(blacklist.getUserId());
		return blacklist;
	}
}