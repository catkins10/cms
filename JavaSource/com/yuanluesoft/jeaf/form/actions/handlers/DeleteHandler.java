package com.yuanluesoft.jeaf.form.actions.handlers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.lock.service.LockService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author chuan
 *
 */
public class DeleteHandler {
	private FormAction formAction;
	
	public DeleteHandler(FormAction formAction) {
		this.formAction = formAction;
	}
	
	/**
	 * 执行删除操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param forwardName
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeDeleteAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult, String forwardName) throws Exception {
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
		}
		try {
			formAction.delete(form, request, response, actionResult);
    	}
    	catch(Exception e) {
    		return formAction.transactException(e, mapping, form, request, response, true);
        }
    	return mapping.findForward(forwardName==null ? "result" : forwardName);
    }
	
	/**
	 * 删除
	 * @param form
	 * @param request
	 * @param response
	 * @param response
	 * @param actionResult
	 * @param mapping
	 * @return
	 * @throws Exception
	 */
	public void delete(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToDelete = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToDelete, request);
		LockService recordLockService = (LockService)formAction.getService("lockService");
		//锁定记录
		recordLockService.lock("" + formToDelete.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
		Record record = formAction.loadRecord(formToDelete, formDefine, formToDelete.getId(), sessionInfo, request);
		//权限控制
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, FormAction.OPEN_MODE_EDIT, sessionInfo);
		formAction.checkDeletePrivilege(formToDelete, request, record, acl, sessionInfo);
		//计费控制
		formAction.checkCharge(formToDelete, request, record, FormAction.OPEN_MODE_EDIT, acl, sessionInfo);
		//写入日志
		formAction.logAction(formDefine.getApplicationName(), formToDelete.getId(), record, sessionInfo, "delete", request);
		try {
			//删除数据
			formAction.deleteRecord(formToDelete, formDefine, record, request, response, sessionInfo);
		}
		catch(ExchangeException exe) {
			formAction.reloadForm(formToDelete, record, formAction.getOpenMode(formToDelete, request), acl, sessionInfo, request, response);
			throw exe;
		}
		//从会话中删除权限记录
		formAction.getRecordControlService().unregistRecordAccessLevel(formToDelete.getId(), request.getSession());
		//解锁
		recordLockService.unlock("" + formToDelete.getId(), sessionInfo.getUserId());
		//设置刷新父窗口的脚本
		formToDelete.setRefeshOpenerScript(formAction.generateRefreshOpenerScript(formToDelete, record, FormAction.OPEN_MODE_EDIT, "delete", actionResult, request, sessionInfo));
		//设置操作结果页面
		formAction.setResultPage(formToDelete, null, FormAction.OPEN_MODE_EDIT, "delete", (actionResult==null ? "删除成功" : actionResult), request, sessionInfo);
		//解锁
		formAction.unlock(formToDelete, request, FormAction.OPEN_MODE_EDIT, sessionInfo);
		//清除请求编码
		FormSecurityService formSecurityService = (FormSecurityService)formAction.getService("formSecurityService");
		formSecurityService.removeRequest(formToDelete.getRequestCode());
		formToDelete.setRequestCode(null);
		formToDelete.setReloadPageURL("javascript:window.close();");
	}
}