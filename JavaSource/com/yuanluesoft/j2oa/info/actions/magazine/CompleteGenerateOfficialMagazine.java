package com.yuanluesoft.j2oa.info.actions.magazine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.info.forms.Magazine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazine;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CompleteGenerateOfficialMagazine extends MagazineAction {
    
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
		InfoMagazine magazine = (InfoMagazine)record;
		magazine.setGenerateDate(DateTimeUtils.date()); //生成日期
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Magazine magazineForm = (Magazine)form;
		//保存正文
		InfoService infoService = (InfoService)getService("infoService");
		infoService.saveMagazineBody(magazine, request);
		//通知工作流引擎,“生成正式刊物”操作执行完毕
		getWorkflowExploitService().completeAction(magazineForm.getWorkflowInstanceId(), magazineForm.getWorkItemId(), "生成正式刊物", getWorkflowSessionInfo(form, sessionInfo));
		return record;
	}
}