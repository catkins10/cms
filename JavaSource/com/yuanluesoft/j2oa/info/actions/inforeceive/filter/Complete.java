package com.yuanluesoft.j2oa.info.actions.inforeceive.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.info.forms.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoReceive;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Complete extends InfoFilterAction {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "筛选完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		InfoReceive infoReceive = (InfoReceive)record;
		InfoFilter infoFilterForm = (InfoFilter)form;
		InfoService infoService = (InfoService)getService("infoService");
		infoService.filterInfo(infoReceive, infoFilterForm.getMagazineIds(), infoFilterForm.getLevel(), infoFilterForm.getIsBrief(), infoFilterForm.getIsVerified(), infoFilterForm.getIsCircular(), infoFilterForm.getFilterOpinion(), sessionInfo);
		return record;
	}
}