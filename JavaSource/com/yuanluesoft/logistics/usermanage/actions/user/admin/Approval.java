package com.yuanluesoft.logistics.usermanage.actions.user.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;

/**
 * 
 * @author linchuan
 *
 */
public class Approval extends UserAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "完成审核", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.usermanage.actions.user.admin.UserAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsUser user = (LogisticsUser)record;
		user.setIsHalt('0'); //是否停用
		user.setIsApproval('0'); //待审核
		user.setApproverId(sessionInfo.getUserId()); //审核人ID
		user.setApprover(sessionInfo.getUserName()); //审核人姓名
		user.setApproverIP(request.getRemoteAddr()); //审核人IP
		user.setApprovalTime(DateTimeUtils.now()); //审核时间
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}