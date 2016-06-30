package com.yuanluesoft.onlinesignup.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.onlinesignup.pojo.admin.SignUp;

/**
 * 
 * @author zyh
 *
 */
public class Audit extends SignUpAction {
	
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null,null, null);
    }

	/* （非 Javadoc）
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains("audit")&&!acl.contains("report")&& !acl.contains("application_manager") && !acl.contains("manageUnit_signup")){
			throw new PrivilegeException();
		}
		return;
	}

	/* （非 Javadoc）
	 * @see com.yuanluesoft.j2oa.book.actions.book.BookAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SignUp signUp = (SignUp)record;
		if("/onlinesignup/admin/doAudit.shtml".equals(request.getServletPath())){//通过审核
			signUp.setStatus(1);
			signUp.setAudited(DateTimeUtils.now());
			signUp.setAuditor(sessionInfo.getUserName());
			signUp.setAuditorId(sessionInfo.getUserId());
			form.setActionResult("审核完成");
		}
		else if("/onlinesignup/admin/doAuditFailure.shtml".equals(request.getServletPath())){//审核不通过
			signUp.setStatus(2);
			signUp.setAudited(DateTimeUtils.now());
			signUp.setAuditor(sessionInfo.getUserName());
			signUp.setAuditorId(sessionInfo.getUserId());
			form.setActionResult("审核完成");
		}else if("/onlinesignup/admin/submitToAudit.shtml".equals(request.getServletPath())){//提交审核
			signUp.setStatus(4);
			form.setActionResult("提交成功");
		}else if("/onlinesignup/admin/unAudit.shtml".equals(request.getServletPath())){
			signUp.setStatus(4);
			signUp.setUnAudited(DateTimeUtils.now());
			signUp.setUnAuditor(sessionInfo.getUserName());
			form.setActionResult("撤销成功，信息已退回到待审列表中。");
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
    
    
    
}