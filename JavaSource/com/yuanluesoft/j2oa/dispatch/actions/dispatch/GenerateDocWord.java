package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.service.DispatchDocWordService;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class GenerateDocWord extends DispatchAction {
	   
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo sessionInfo) throws Exception {
		DispatchDocWordService docWordService = (DispatchDocWordService)getService("dispatchDocWordService");
		Dispatch dispatch = (Dispatch)record;
		com.yuanluesoft.j2oa.dispatch.forms.Dispatch formDispatch = (com.yuanluesoft.j2oa.dispatch.forms.Dispatch)form;
		docWordService.generateDocWord(dispatch, formDispatch.isWorkflowTest());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}