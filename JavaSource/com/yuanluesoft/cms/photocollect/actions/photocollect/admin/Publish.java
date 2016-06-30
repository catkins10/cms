package com.yuanluesoft.cms.photocollect.actions.photocollect.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.photocollect.pojo.PhotoCollect;
import com.yuanluesoft.cms.photocollect.service.PhotoCollectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Publish extends PhotoCollectAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executePublishAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		PhotoCollect photoCollect = (PhotoCollect)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		PhotoCollectService photoCollectService = (PhotoCollectService)getService("photoCollectService");
		photoCollectService.synchSiteColumns(photoCollect, sessionInfo);
		return photoCollect;
	}
}