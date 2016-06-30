package com.yuanluesoft.logistics.supply.actions.supply.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupply;

/**
 * 
 * @author linchuan
 *
 */
public class Unissue extends SupplyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "撤销成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.supply.actions.supply.admin.SupplyAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsSupply supply = (LogisticsSupply)record;
		supply.setIssue(0);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}