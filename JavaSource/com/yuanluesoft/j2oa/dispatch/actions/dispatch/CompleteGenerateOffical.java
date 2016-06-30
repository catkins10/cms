package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.dispatch.forms.Dispatch;
import com.yuanluesoft.j2oa.dispatch.service.DispatchService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CompleteGenerateOffical extends DispatchAction {
    
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, false, null, "生成完毕！", null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch dispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
		dispatch.setGenerateDate(DateTimeUtils.date()); //生成日期
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Dispatch dispatchForm = (Dispatch)form;
		//保存HTML正文
		DispatchService dispatchService = (DispatchService)getService("dispatchService");
		dispatchService.saveBody((com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record, request);
		//保存办理单
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		String path = attachmentService.getSavePath("j2oa/dispatch", "official", dispatchForm.getId(), true);
		FileUtils.saveStringToFile(path + "办理单.html", dispatchForm.getHandling(), "utf-8", true);
		//通知工作流引擎,“生成正式文件”操作执行完毕
		getWorkflowExploitService().completeAction(dispatchForm.getWorkflowInstanceId(), dispatchForm.getWorkItemId(), "生成正式文件", getWorkflowSessionInfo(form, sessionInfo));
		return record;
	}
}